package com.ymmihw.spring.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.google.common.collect.ImmutableSet;
import com.ymmihw.spring.data.cassandra.CassandraTemplateIntegrationTest.DockerCassandraConfig;
import com.ymmihw.spring.data.cassandra.model.Book;
import com.ymmihw.spring.data.cassandra.model.BookKey;
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
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

@ContextConfiguration(classes = DockerCassandraConfig.class)
@SpringBootTest
@Testcontainers
public class CassandraTemplateIntegrationTest {
  @Configuration
  @PropertySource(value = {"classpath:cassandra.properties"})
  @EnableCassandraRepositories(basePackages = "com.ymmihw.spring.data.cassandra.repository")
  public static class DockerCassandraConfig extends AbstractCassandraConfiguration {
    @Autowired
    private Environment environment;

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

  @Container
  public static CassandraContainer<?> container = new CassandraContainer<>("cassandra:3.11.6");

  private static final Log LOGGER = LogFactory.getLog(CassandraTemplateIntegrationTest.class);

  public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testKeySpace "
      + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

  public static final String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";

  public static final String DATA_TABLE_NAME = "book";

  @Autowired
  private CassandraAdminTemplate adminTemplate;

  @Autowired
  private CassandraTemplate cassandraTemplate;

  //

  @BeforeAll
  public static void startCassandraEmbedded() throws InterruptedException, IOException {
    container.start();
    Cluster cluster =
        Cluster.builder().withoutMetrics().addContactPoints(container.getContainerIpAddress())
            .withPort(container.getFirstMappedPort()).build();
    final Session session = cluster.connect();
    session.execute(KEYSPACE_CREATION_QUERY);
    session.execute(KEYSPACE_ACTIVATE_QUERY);
    LOGGER.info("KeySpace created and activated.");
    Thread.sleep(5000);
  }

  @BeforeEach
  public void createTable() throws InterruptedException, IOException {
    adminTemplate.createTable(true, CqlIdentifier.fromCql(DATA_TABLE_NAME), Book.class,
        new HashMap<String, Object>());
  }

  @Test
  public void whenSavingBook_thenAvailableOnRetrieval() {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    cassandraTemplate.insert(javaBook);
    final Book retrievedBook =
        cassandraTemplate.selectOne(query(where("key.title").is("Head First Java"))
            .and(where("key.publisher").is("O'Reilly Media")).limit(10), Book.class);
    assertEquals(javaBook.getId(), retrievedBook.getId());
  }

  @Test
  public void whenSavingBooks_thenAllAvailableOnRetrieval() {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    final Book dPatternBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head Design Patterns", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    final List<Book> bookList = new ArrayList<>();
    bookList.add(javaBook);
    bookList.add(dPatternBook);
    cassandraTemplate.batchOps().insert(bookList).execute();

    final List<Book> retrievedBooks = cassandraTemplate.select(query().limit(10), Book.class);
    assertThat(retrievedBooks.size(), is(2));
    assertEquals(javaBook.getId(), retrievedBooks.get(0).getId());
    assertEquals(dPatternBook.getId(), retrievedBooks.get(1).getId());
  }

  @Test
  public void whenUpdatingBook_thenShouldUpdatedOnRetrieval() {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    cassandraTemplate.insert(javaBook);
    final Book retrievedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    retrievedBook.setTags(ImmutableSet.of("Java", "Programming"));
    cassandraTemplate.update(retrievedBook);
    final Book retrievedUpdatedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    assertEquals(retrievedBook.getTags(), retrievedUpdatedBook.getTags());
  }

  @Test
  public void whenDeletingASelectedBook_thenNotAvailableOnRetrieval() throws InterruptedException {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "OReilly Media"),
            ImmutableSet.of("Computer", "Software"));
    cassandraTemplate.insert(javaBook);
    cassandraTemplate.delete(javaBook);
    final Book retrievedUpdatedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    assertNull(retrievedUpdatedBook);
  }

  @Test
  public void whenDeletingAllBooks_thenNotAvailableOnRetrieval() {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    final Book dPatternBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head Design Patterns", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    cassandraTemplate.insert(javaBook);
    cassandraTemplate.insert(dPatternBook);
    cassandraTemplate.truncate(Book.class);
    final Book retrievedUpdatedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    assertNull(retrievedUpdatedBook);
  }

  @Test
  public void whenAddingBooks_thenCountShouldBeCorrectOnRetrieval() {
    final Book javaBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head First Java", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    final Book dPatternBook =
        new Book(new BookKey(UUIDs.timeBased(), "Head Design Patterns", "O'Reilly Media"),
            ImmutableSet.of("Computer", "Software"));
    cassandraTemplate.insert(javaBook);
    cassandraTemplate.insert(dPatternBook);
    final long bookCount = cassandraTemplate.count(Book.class);
    assertEquals(2, bookCount);
  }

  @AfterEach
  public void dropTable() {
    adminTemplate.dropTable(CqlIdentifier.fromCql(DATA_TABLE_NAME));
  }
}
