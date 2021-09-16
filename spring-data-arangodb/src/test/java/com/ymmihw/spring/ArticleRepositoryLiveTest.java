package com.ymmihw.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ymmihw.spring.model.Article;
import com.ymmihw.spring.repository.ArticleRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ContextConfiguration(
    classes = {
      //        ArangoDbDockerConfiguration.class
    },
    loader = AnnotationConfigContextLoader.class)
@Testcontainers
public class ArticleRepositoryLiveTest {
  @Container private ArangoContainer container = ArangoContainer.getInstance();

  @BeforeEach
  public void beforeAll() {
    System.setProperty(
        "arangodb.hosts", container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
    System.setProperty("arangodb.user", "root");
    System.setProperty("arangodb.password", "password");
  }

  //  @Configuration
  //  @EnableArangoRepositories(basePackages = {"com.ymmihw.spring"})
  //  public static class ArangoDbDockerConfiguration implements ArangoConfiguration {
  //
  //    @Override
  //    public ArangoDB.Builder arango() {
  //      return new ArangoDB.Builder()
  //          .host(container.getContainerIpAddress(), container.getFirstMappedPort())
  //          .user("root")
  //          .password("password");
  //    }
  //
  //    @Override
  //    public String database() {
  //      return "baeldung-database";
  //    }
  //  }

  @Autowired ArticleRepository articleRepository;

  @Test
  public void givenNewArticle_whenSaveInArangoDb_thenDataIsCorrect() {
    Article newArticle =
        new Article(
            "ArangoDb with Spring Data",
            "Baeldung Writer",
            ZonedDateTime.now(),
            "<html>Some HTML content</html>");

    Article savedArticle = articleRepository.save(newArticle);

    assertNotNull(savedArticle.getId());
    assertNotNull(savedArticle.getArangoId());

    assertEquals(savedArticle.getName(), newArticle.getName());
    assertEquals(savedArticle.getAuthor(), newArticle.getAuthor());
    assertEquals(savedArticle.getPublishDate(), newArticle.getPublishDate());
    assertEquals(savedArticle.getHtmlContent(), newArticle.getHtmlContent());
  }

  @Test
  public void givenArticleId_whenReadFromArangoDb_thenDataIsCorrect() {
    Article newArticle =
        new Article(
            "ArangoDb with Spring Data",
            "Baeldung Writer",
            ZonedDateTime.now(),
            "<html>Some HTML content</html>");

    Article savedArticle = articleRepository.save(newArticle);

    String articleId = savedArticle.getId();

    Optional<Article> article = articleRepository.findById(articleId);
    assertTrue(article.isPresent());

    Article foundArticle = article.get();

    assertEquals(foundArticle.getId(), articleId);
    assertEquals(foundArticle.getArangoId(), savedArticle.getArangoId());
    assertEquals(foundArticle.getName(), savedArticle.getName());
    assertEquals(foundArticle.getAuthor(), savedArticle.getAuthor());
    assertEquals(foundArticle.getPublishDate(), savedArticle.getPublishDate());
    assertEquals(foundArticle.getHtmlContent(), savedArticle.getHtmlContent());
  }

  @Test
  public void givenArticleId_whenDeleteFromArangoDb_thenDataIsGone() {
    Article newArticle =
        new Article(
            "ArangoDb with Spring Data",
            "Baeldung Writer",
            ZonedDateTime.now(),
            "<html>Some HTML content</html>");

    Article savedArticle = articleRepository.save(newArticle);

    String articleId = savedArticle.getId();

    articleRepository.deleteById(articleId);

    Optional<Article> article = articleRepository.findById(articleId);
    assertFalse(article.isPresent());
  }

  @Test
  public void givenAuthorName_whenGetByAuthor_thenListOfArticles() {
    Article newArticle =
        new Article(
            "ArangoDb with Spring Data",
            "Baeldung Writer",
            ZonedDateTime.now(),
            "<html>Some HTML content</html>");
    newArticle.setAuthor(UUID.randomUUID().toString());
    articleRepository.save(newArticle);

    Iterable<Article> articlesByAuthor = articleRepository.findByAuthor(newArticle.getAuthor());
    List<Article> articlesByAuthorList = new ArrayList<>();
    articlesByAuthor.forEach(articlesByAuthorList::add);

    assertEquals(1, articlesByAuthorList.size());

    Article foundArticle = articlesByAuthorList.get(0);
    assertEquals(foundArticle.getName(), newArticle.getName());
    assertEquals(foundArticle.getAuthor(), newArticle.getAuthor());
    assertEquals(foundArticle.getPublishDate(), newArticle.getPublishDate());
    assertEquals(foundArticle.getHtmlContent(), newArticle.getHtmlContent());
  }
}
