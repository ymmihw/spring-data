package com.ymmihw.spring.data.elasticsearch.introduction;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
  public static class DockerClient {

    @Bean
    public Client client() {
      Settings settings = Settings.builder().put("client.transport.sniff", false).build();
      @SuppressWarnings("resource")
      PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
      try {
        TransportClient addTransportAddress = preBuiltTransportClient.addTransportAddress(
            new TransportAddress(InetAddress.getByName(container.getContainerIpAddress()),
                container.getMappedPort(9300)));
        return addTransportAddress;
      } catch (final UnknownHostException ioex) {
        throw new RuntimeException(ioex);
      }

    }
  }

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Autowired
  private ArticleService articleService;

  private final Author johnSmith = new Author("John Smith");
  private final Author johnDoe = new Author("John Doe");

  @Before
  public void before() {
    elasticsearchTemplate.deleteIndex(Article.class);
    elasticsearchTemplate.createIndex(Article.class);
    elasticsearchTemplate.putMapping(Article.class);
    elasticsearchTemplate.refresh(Article.class);

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

    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withFilter(regexpQuery("title", ".*data.*")).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);

    assertEquals(1, articles.size());
  }

  @Test
  public void givenSavedDoc_whenTitleUpdated_thenCouldFindByUpdatedTitle() {
    final SearchQuery searchQuery =
        new NativeSearchQueryBuilder().withQuery(fuzzyQuery("title", "serch")).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);

    assertEquals(1, articles.size());

    final Article article = articles.get(0);
    final String newTitle = "Getting started with Search Engines";
    article.setTitle(newTitle);
    articleService.save(article);

    assertTrue(articleService.findById(article.getId()).isPresent());
    assertEquals(newTitle, articleService.findById(article.getId()).get().getTitle());
  }

  @Test
  public void givenSavedDoc_whenDelete_thenRemovedFromIndex() {

    final String articleTitle = "Spring Data Elasticsearch";

    final SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", articleTitle).minimumShouldMatch("75%")).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
    final long count = articleService.count();

    articleService.delete(articles.get(0));

    assertEquals(count - 1, articleService.count());
  }

  @Test
  public void givenSavedDoc_whenOneTermMatches_thenFindByTitle() {
    final SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(matchQuery("title", "Search engines").operator(AND)).build();
    final List<Article> articles = elasticsearchTemplate.queryForList(searchQuery, Article.class);
    assertEquals(1, articles.size());
  }
}
