package com.ymmihw.spring.data.elasticsearch.tags;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.elasticsearch.MyElasticsearchContainer;
import com.ymmihw.spring.data.elasticsearch.tags.ElasticSearchIntegrationTest.DockerClient;
import com.ymmihw.spring.data.elasticsearch.tags.config.Config;
import com.ymmihw.spring.data.elasticsearch.tags.model.Article;
import com.ymmihw.spring.data.elasticsearch.tags.model.Author;
import com.ymmihw.spring.data.elasticsearch.tags.service.ArticleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DockerClient.class, Config.class})
public class ElasticSearchIntegrationTest {
  @ClassRule
  public static MyElasticsearchContainer container = MyElasticsearchContainer.getInstance();

  @Configuration
  public static class DockerClient {
    @Bean
    public Client client() {
      Settings settings = Settings.builder().put("client.transport.sniff", false).build();
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
    assertThat(articles, containsInAnyOrder(hasProperty("id", is("1")), hasProperty("id", is("3")),
        hasProperty("id", is("4"))));
  }

  @Test
  public void givenTagFilterQuery_whenSearchByAuthorsName_thenArticleIsFound() {
    final Page<Article> articles = articleService.findByAuthorsNameAndFilteredTagQuery("Doe",
        "elasticsearch", PageRequest.of(0, 10));
    assertEquals(2L, articles.getTotalElements());
    assertThat(articles,
        containsInAnyOrder(hasProperty("id", is("1")), hasProperty("id", is("4"))));
  }

  @Test
  public void givenTagFilterQuery_whenSearchByTagUsingDeclaredQuery_thenArticleIsFound() {
    Page<Article> articles =
        articleService.findByTagUsingDeclaredQuery("elasticsearch", PageRequest.of(0, 10));
    assertEquals(3L, articles.getTotalElements());
    assertThat(articles, containsInAnyOrder(hasProperty("id", is("1")), hasProperty("id", is("3")),
        hasProperty("id", is("4"))));
  }

}
