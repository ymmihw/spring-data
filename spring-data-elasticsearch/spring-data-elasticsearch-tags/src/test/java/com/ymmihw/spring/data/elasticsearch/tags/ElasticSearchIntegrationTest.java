package com.ymmihw.spring.data.elasticsearch.tags;

import com.ymmihw.spring.data.elasticsearch.MyElasticsearchContainer;
import com.ymmihw.spring.data.elasticsearch.tags.ElasticSearchIntegrationTest.DockerClient;
import com.ymmihw.spring.data.elasticsearch.tags.config.Config;
import com.ymmihw.spring.data.elasticsearch.tags.model.Article;
import com.ymmihw.spring.data.elasticsearch.tags.model.Author;
import com.ymmihw.spring.data.elasticsearch.tags.service.ArticleService;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@ContextConfiguration(
    classes = {Config.class, DockerClient.class},
    loader = AnnotationConfigContextLoader.class)
@Testcontainers
public class ElasticSearchIntegrationTest {
  @Container
  public static MyElasticsearchContainer container = MyElasticsearchContainer.getInstance();

  @Configuration
  public static class DockerClient extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
      final ClientConfiguration clientConfiguration =
          ClientConfiguration.builder()
              .connectedTo(container.getContainerIpAddress() + ":" + container.getMappedPort(9200))
              .build();

      return RestClients.create(clientConfiguration).rest();
    }
  }

  @Autowired private ElasticsearchOperations elasticsearchOperations;

  @Autowired private ArticleService articleService;

  private final Author johnSmith = new Author("John Smith");
  private final Author johnDoe = new Author("John Doe");

  @BeforeEach
  public void before() {
    IndexOperations indexOperations = elasticsearchOperations.indexOps(Article.class);
    indexOperations.delete();
    indexOperations.create();
    indexOperations.putMapping();
    indexOperations.refresh();
    // don't call putMapping() to test the default mappings

    Article article = new Article("Spring Data Elasticsearch");
    article.setId("1");
    article.setAuthors(asList(johnSmith, johnDoe));
    article.setTags("elasticsearch", "spring data");
    articleService.save(article);

    article = new Article("Search engines");
    article.setId("2");
    article.setAuthors(asList(johnDoe));
    article.setTags("search engines", "tutorial");
    articleService.save(article);

    article = new Article("Second Article About Elasticsearch");
    article.setId("3");
    article.setAuthors(asList(johnSmith));
    article.setTags("elasticsearch", "spring data");
    articleService.save(article);

    article = new Article("Elasticsearch Tutorial");
    article.setId("4");
    article.setAuthors(asList(johnDoe));
    article.setTags("elasticsearch");
    articleService.save(article);
  }

  @Test
  public void givenTagFilterQuery_whenSearchByTag_thenArticleIsFound() {
    final Page<Article> articles =
        articleService.findByFilteredTagQuery("elasticsearch", PageRequest.of(0, 10));
    assertEquals(3L, articles.getTotalElements());
    assertThat(
        articles,
        containsInAnyOrder(
            hasProperty("id", is("1")), hasProperty("id", is("3")), hasProperty("id", is("4"))));
  }

  @Test
  public void givenTagFilterQuery_whenSearchByAuthorsName_thenArticleIsFound() {
    final Page<Article> articles =
        articleService.findByAuthorsNameAndFilteredTagQuery(
            "Doe", "elasticsearch", PageRequest.of(0, 10));
    assertEquals(2L, articles.getTotalElements());
    assertThat(
        articles, containsInAnyOrder(hasProperty("id", is("1")), hasProperty("id", is("4"))));
  }

  @Test
  public void givenTagFilterQuery_whenSearchByTagUsingDeclaredQuery_thenArticleIsFound() {
    Page<Article> articles =
        articleService.findByTagUsingDeclaredQuery("elasticsearch", PageRequest.of(0, 10));
    assertEquals(3L, articles.getTotalElements());
    assertThat(
        articles,
        containsInAnyOrder(
            hasProperty("id", is("1")), hasProperty("id", is("3")), hasProperty("id", is("4"))));
  }
}
