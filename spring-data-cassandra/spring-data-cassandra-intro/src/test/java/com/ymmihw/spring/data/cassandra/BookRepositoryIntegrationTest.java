package com.ymmihw.spring.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableSet;
import com.ymmihw.spring.data.cassandra.BookRepositoryIntegrationTest.DockerCassandraConfig;
import com.ymmihw.spring.data.cassandra.model.Book;
import com.ymmihw.spring.data.cassandra.model.BookKey;
import com.ymmihw.spring.data.cassandra.repository.BookRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@ContextConfiguration(
    classes = {DockerCassandraConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Testcontainers
public class BookRepositoryIntegrationTest {

  @Configuration
  @PropertySource(value = {"classpath:cassandra.properties"})
  @EnableCassandraRepositories(basePackages = "com.ymmihw.spring.data.cassandra.repository")
  public static class DockerCassandraConfig extends AbstractCassandraConfiguration {
    @Autowired private Environment environment;

    @Override
    protected String getKeyspaceName() {
      return environment.getProperty("cassandra.keyspace");
    }

    @Override
    protected String getContactPoints() {
      return container.getContainerIpAddress();
    }

    @Override
    protected int getPort() {
      return container.getFirstMappedPort();
    }

    @Override
    protected String getLocalDataCenter() {
      return "datacenter1";
    }
  }

  private static final Log LOGGER = LogFactory.getLog(BookRepositoryIntegrationTest.class);

  public static final String KEYSPACE_CREATION_QUERY =
      "CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

  public static final String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";

  public static final String DATA_TABLE_NAME = "book";

  @Autowired private BookRepository bookRepository;

  @Autowired private CassandraAdminOperations adminTemplate;

  @Container
  public static CassandraContainer<?> container = new CassandraContainer<>("cassandra:3.11.6");

  @BeforeAll
  public static void startCassandraEmbedded() throws InterruptedException, IOException {
    container.start();
    Cluster cluster =
        Cluster.builder()
            .withoutMetrics()
            .addContactPoints(container.getContainerIpAddress())
            .withPort(container.getFirstMappedPort())
            .build();
    final Session session = cluster.connect();
    session.execute(KEYSPACE_CREATION_QUERY);
    session.execute(KEYSPACE_ACTIVATE_QUERY);
    LOGGER.info("KeySpace created and activated.");
    Thread.sleep(5000);
  }

  @BeforeEach
  public void createTable() throws InterruptedException, IOException {
    adminTemplate.createTable(
        true, CqlIdentifier.fromCql(DATA_TABLE_NAME), Book.class, new HashMap<String, Object>());
  }

  @Test
  public void whenSavingBook_thenAvailableOnRetrieval() {
    final Book javaBook =
        new Book(
            new BookKey(UUID.randomUUID(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    bookRepository.save(javaBook);
    final Iterable<Book> books =
        bookRepository.findByTitleAndPublisher("Head First Java", "O'Reilly Media");
    assertEquals(javaBook.getId(), books.iterator().next().getId());
  }

  @Test
  public void whenUpdatingBooks_thenAvailableOnRetrieval() {
    final Book javaBook =
        new Book(
            new BookKey(UUID.randomUUID(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    bookRepository.save(javaBook);
    final Iterable<Book> books =
        bookRepository.findByTitleAndPublisher("Head First Java", "O'Reilly Media");
    javaBook.setTitle("Head First Java Second Edition");
    bookRepository.save(javaBook);
    final Iterable<Book> updateBooks =
        bookRepository.findByTitleAndPublisher("Head First Java Second Edition", "O'Reilly Media");
    assertEquals(javaBook.getTitle(), updateBooks.iterator().next().getTitle());
  }

  @Test
  public void whenDeletingExistingBooks_thenNotAvailableOnRetrieval() {
    assertThrows(
        java.util.NoSuchElementException.class,
        () -> {
          final Book javaBook =
              new Book(
                  new BookKey(UUID.randomUUID(), "Head First Java", "O'Reilly Media"),
                  ImmutableSet.of("Computer", "Software"));
          bookRepository.save(javaBook);
          bookRepository.delete(javaBook);
          final Iterable<Book> books =
              bookRepository.findByTitleAndPublisher("Head First Java", "O'Reilly Media");
          assertNotEquals(javaBook.getId(), books.iterator().next().getId());
        });
  }

  @Test
  public void whenSavingBooks_thenAllShouldAvailableOnRetrieval() {
    final Book javaBook =
        new Book(
            new BookKey(UUID.randomUUID(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    final Book dPatternBook =
        new Book(
            new BookKey(UUID.randomUUID(), "Head Design Patterns", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    bookRepository.saveAll(ImmutableSet.of(javaBook, dPatternBook));
    final Iterable<Book> books = bookRepository.findAll();
    int bookCount = 0;
    for (final Book book : books) {
      bookCount++;
    }
    assertEquals(bookCount, 2);
  }

  @AfterEach
  public void dropTable() {
    adminTemplate.dropTable(CqlIdentifier.fromCql(DATA_TABLE_NAME));
  }
}
