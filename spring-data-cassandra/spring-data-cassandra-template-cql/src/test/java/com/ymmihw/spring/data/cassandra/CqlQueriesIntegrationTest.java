package com.ymmihw.spring.data.cassandra;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.data.cassandra.core.query.Query.query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.CassandraContainer;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.google.common.collect.ImmutableSet;
import com.ymmihw.spring.data.cassandra.CqlQueriesIntegrationTest.DockerCassandraConfig;
import com.ymmihw.spring.data.cassandra.model.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DockerCassandraConfig.class)
public class CqlQueriesIntegrationTest {
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

  @ClassRule
  public static CassandraContainer<?> container = new CassandraContainer<>("cassandra:3.11.6");


  private static final Log LOGGER = LogFactory.getLog(CqlQueriesIntegrationTest.class);

  public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testKeySpace "
      + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

  public static final String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";

  public static final String DATA_TABLE_NAME = "book";

  @Autowired
  private CassandraAdminTemplate adminTemplate;

  @Autowired
  private CassandraTemplate cassandraTemplate;

  @BeforeClass
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

  @Before
  public void createTable() throws InterruptedException, IOException {
    adminTemplate.createTable(true, CqlIdentifier.fromCql(DATA_TABLE_NAME), Book.class,
        new HashMap<String, Object>());
  }

  @Test
  public void whenSavingBook_thenAvailableOnRetrieval_usingQueryBuilder() {
    final UUID uuid = UUIDs.timeBased();
    final String insertCql = "insert into book (id, title, publisher, tags) values " + "(" + uuid
        + ", 'Head First Java', 'OReilly Media', {'Software'})";
    cassandraTemplate.getCqlOperations().execute(insertCql);
    final Book retrievedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    assertEquals(uuid, retrievedBook.getId());
  }

  @Test
  public void whenSavingBook_thenAvailableOnRetrieval_usingCQLStatements() {
    final UUID uuid = UUIDs.timeBased();
    final String insertCql = "insert into book (id, title, publisher, tags) values " + "(" + uuid
        + ", 'Head First Java', 'OReilly Media', {'Software'})";
    cassandraTemplate.getCqlOperations().execute(insertCql);
    final Book retrievedBook = cassandraTemplate.selectOne(query().limit(10), Book.class);
    assertEquals(uuid, retrievedBook.getId());
  }

  // @Test
  public void whenSavingBook_thenAvailableOnRetrieval_usingPreparedStatements()
      throws InterruptedException {
    final UUID uuid = UUIDs.timeBased();
    final String insertPreparedCql =
        "insert into book (id, title, publisher, tags) values (?, ?, ?, ?)";
    final List<Object> singleBookArgsList = new ArrayList<>();
    final List<List<?>> bookList = new ArrayList<>();
    singleBookArgsList.add(uuid);
    singleBookArgsList.add("Head First Java");
    singleBookArgsList.add("OReilly Media");
    singleBookArgsList.add(ImmutableSet.of("Software"));
    bookList.add(singleBookArgsList);
    cassandraTemplate.getCqlOperations().execute(insertPreparedCql, bookList);
    // This may not be required, just added to avoid any transient issues
    Thread.sleep(5000);
    final Book retrievedBook = cassandraTemplate.selectOne(query(), Book.class);
    assertEquals(uuid, retrievedBook.getId());
  }

  @After
  public void dropTable() {
    adminTemplate.dropTable(CqlIdentifier.fromCql(DATA_TABLE_NAME));
  }

}
