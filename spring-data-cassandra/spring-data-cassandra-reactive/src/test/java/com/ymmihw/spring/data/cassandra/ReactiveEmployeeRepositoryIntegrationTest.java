package com.ymmihw.spring.data.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.ymmihw.spring.data.cassandra.ReactiveEmployeeRepositoryIntegrationTest.DockerCassandraConfig;
import com.ymmihw.spring.data.cassandra.model.Employee;
import com.ymmihw.spring.data.cassandra.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

@SpringBootTest
@Testcontainers
@ContextConfiguration(
    classes = {DockerCassandraConfig.class, SpringDataCassandraReactiveApplication.class})
public class ReactiveEmployeeRepositoryIntegrationTest {
  @Configuration
  public static class DockerCassandraConfig extends AbstractCassandraConfiguration {
    @Override
    protected String getKeyspaceName() {
      return "practice";
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

  @Autowired EmployeeRepository repository;

  public static final String KEYSPACE_CREATION_QUERY =
      "CREATE KEYSPACE IF NOT EXISTS practice WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

  public static final String KEYSPACE_ACTIVATE_QUERY = "USE practice;";

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
    session.execute(
        "CREATE TABLE employee(id int PRIMARY KEY,name text,address text,email text,age int);");
    Thread.sleep(5000);
  }

  @BeforeEach
  public void setUp() {

    Flux<Employee> deleteAndInsert =
        repository
            .deleteAll() //
            .thenMany(
                repository.saveAll(
                    Flux.just(
                        new Employee(111, "John Doe", "Delaware", "jdoe@xyz.com", 31),
                        new Employee(222, "Adam Smith", "North Carolina", "asmith@xyz.com", 43),
                        new Employee(333, "Kevin Dunner", "Virginia", "kdunner@xyz.com", 24),
                        new Employee(444, "Mike Lauren", "New York", "mlauren@xyz.com", 41))));

    StepVerifier.create(deleteAndInsert).expectNextCount(4).verifyComplete();
  }

  @Test
  public void givenRecordsAreInserted_whenDbIsQueried_thenShouldIncludeNewRecords() {

    Mono<Long> saveAndCount =
        repository
            .count()
            .doOnNext(System.out::println)
            .thenMany(
                repository.saveAll(
                    Flux.just(
                        new Employee(325, "Kim Jones", "Florida", "kjones@xyz.com", 42),
                        new Employee(654, "Tom Moody", "New Hampshire", "tmoody@xyz.com", 44))))
            .last()
            .flatMap(v -> repository.count())
            .doOnNext(System.out::println);

    StepVerifier.create(saveAndCount).expectNext(6L).verifyComplete();
  }

  @Test
  public void givenAgeForFilter_whenDbIsQueried_thenShouldReturnFilteredRecords() {
    StepVerifier.create(repository.findByAgeGreaterThan(35)).expectNextCount(2).verifyComplete();
  }
}
