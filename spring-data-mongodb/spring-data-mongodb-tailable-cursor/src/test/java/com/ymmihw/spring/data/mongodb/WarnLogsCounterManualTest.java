package com.ymmihw.spring.data.mongodb;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import com.ymmihw.spring.data.mongodb.repository.LogsRepository;
import com.ymmihw.spring.data.mongodb.service.WarnLogsCounter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class WarnLogsCounterManualTest extends BaseTest {
  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() {
      return MongoClients.create(
          "mongodb://" + container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
    }
  }

  @Autowired
  private LogsRepository repository;

  @Autowired
  private ReactiveMongoTemplate template;

  @Before
  public void setUp() {
    template.dropCollection(Log.class).block();
    createCappedCollectionUsingReactiveMongoTemplate(template);
    persistDocument(Log.builder().level(LogLevel.WARN).service("Service 1")
        .message("Initial Warn message").build());
  }

  private void createCappedCollectionUsingReactiveMongoTemplate(
      ReactiveMongoTemplate reactiveMongoTemplate) {
    reactiveMongoTemplate.dropCollection(Log.class).block();
    reactiveMongoTemplate.createCollection(Log.class,
        CollectionOptions.empty().maxDocuments(5).size(1024 * 1024L).capped()).block();
  }

  private void persistDocument(Log log) {
    repository.save(log).block();
  }

  @Test
  public void whenWarnLogsArePersisted_thenTheyAreReceivedByLogsCounter() throws Exception {
    WarnLogsCounter warnLogsCounter = new WarnLogsCounter(template);

    Thread.sleep(1000L); // wait for initialization

    Flux.range(0, 10)
        .map(i -> Log.builder().level(i > 5 ? LogLevel.WARN : LogLevel.INFO).service("some-service")
            .message("some log message").build())
        .map(entity -> repository.save(entity).subscribe()).blockLast();

    Thread.sleep(1000L); // wait to receive all messages from the reactive mongodb driver

    assertThat(warnLogsCounter.count(), is(5));
    warnLogsCounter.close();
  }
}