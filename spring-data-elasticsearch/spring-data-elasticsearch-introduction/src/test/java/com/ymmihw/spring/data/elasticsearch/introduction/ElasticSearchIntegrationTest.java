package com.ymmihw.spring.data.elasticsearch.introduction;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.elasticsearch.MyElasticsearchContainer;
import com.ymmihw.spring.data.elasticsearch.introduction.ElasticSearchIntegrationTest.DockerClient;
import com.ymmihw.spring.data.elasticsearch.introduction.config.Config;
import com.ymmihw.spring.data.elasticsearch.introduction.model.Article;
import com.ymmihw.spring.data.elasticsearch.introduction.model.Author;
import com.ymmihw.spring.data.elasticsearch.introduction.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class, DockerClient.class})
public class ElasticSearchIntegrationTest {

  @ClassRule
  public static MyElasticsearchContainer container = MyElasticsearchContainer.getInstance();

  @Configuration
  public static class DockerClient extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
      final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
          .connectedTo(container.getContainerIpAddress() + ":" + container.getMappedPort(9300))
          .build();

      return RestClients.create(clientConfiguration).rest();
    }
  }

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;


  @Autowired
  private ArticleService articleService;

  private final Author johnSmith = new Author("John Smith");
  private final Author johnDoe = new Author("John Doe");

  @Before
  public void before() {

    elasticsearchOperations.deleteIndex(Article.class);
    elasticsearchOperations.createIndex(Article.class);
    elasticsearchOperations.putMapping(Article.class);
    elasticsearchOperations.refresh(Article.class);

    Article article = new Article("Spring Data Elasticsearch");
    article.setAuthors(asList(johnSmith, johnDoe));
    articleService.save(article);

    article = new Article("Search engines");
    article.setAuthors(asList(johnDoe));
    articleService.save(article);

    article = new Article("Second Article About Elasticsearch");
    article.setAuthors(asList(johnSmith));
    articleService.save(article);
  }

  @Test
  public void givenArticleService_whenSaveArticle_thenIdIsAssigned() {
    final List<Author> authors = asList(johnSmith, johnDoe);

    Article article = new Article("Making Search Elastic");
    article.setAuthors(authors);
    article = articleService.save(article);
    assertNotNull(article.getId());
  }

  @Test
  public void givenPersistedArticles_whenSearchByAuthorsName_thenRightFound() {

    final Page<Article> articleByAuthorName =
        articleService.findByAuthorName(johnSmith.getName(), PageRequest.of(0, 10));
    assertEquals(2L, articleByAuthorName.getTotalElements());
  }

  @Test
  public void givenCustomQuery_whenSearchByAuthorsName_thenArticleIsFound() {

    final Page<Article> articleByAuthorName =
        articleService.findByAuthorNameUsingCustomQuery(johnSmith.getName(), PageRequest.of(0, 10));
    assertEquals(3L, articleByAuthorName.getTotalElements());
  }

  @Test
  public void givenPersistedArticles_whenUseRegexQuery_thenRightArticlesFound() {

    final Query searchQuery =
        new NativeSearchQueryBuilder().withFilter(regexpQuery("title", ".*data.*")).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();

    assertEquals(1, articles.size());
  }

  @Test
  public void givenSavedDoc_whenTitleUpdated_thenCouldFindByUpdatedTitle() {
    final Query searchQuery =
        new NativeSearchQueryBuilder().withQuery(fuzzyQuery("title", "serch")).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();

    assertEquals(1, articles.size());

    final SearchHit<Article> searchHit = articles.get(0);
    final String newTitle = "Getting started with Search Engines";
    Article article = searchHit.getContent();
    article.setTitle(newTitle);
    articleService.save(article);

    assertTrue(articleService.findById(article.getId()).isPresent());
    assertEquals(newTitle, articleService.findById(article.getId()).get().getTitle());
  }

  @Test
  public void givenSavedDoc_whenDelete_thenRemovedFromIndex() {

    final String articleTitle = "Spring Data Elasticsearch";

    final Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", articleTitle).minimumShouldMatch("75%")).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();
    assertEquals(1, articles.size());
    final long count = articleService.count();

    articleService.delete(articles.get(0).getContent());

    assertEquals(count - 1, articleService.count());
  }

  @Test
  public void givenSavedDoc_whenOneTermMatches_thenFindByTitle() {
    final Query searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", "Search engines").operator(AND)).build();
    final List<SearchHit<Article>> articles =
        elasticsearchOperations.search(searchQuery, Article.class).getSearchHits();
    assertEquals(1, articles.size());
  }
}
