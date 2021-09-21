package com.ymmihw.spring.data.mongodb;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import com.ymmihw.spring.data.mongodb.repository.LogsRepository;
import com.ymmihw.spring.data.mongodb.service.WarnLogsCounter;
import reactor.core.publisher.Flux;

public class WarnLogsCounterManualTest extends BaseTest {

  @Autowired private LogsRepository repository;

  @Autowired private ReactiveMongoTemplate template;

  @BeforeEach
  public void setUp() {
    template.dropCollection(Log.class).block();
    createCappedCollectionUsingReactiveMongoTemplate(template);
    persistDocument(
        Log.builder()
            .level(LogLevel.WARN)
            .service("Service 1")
            .message("Initial Warn message")
            .build());
  }

  private void createCappedCollectionUsingReactiveMongoTemplate(
      ReactiveMongoTemplate reactiveMongoTemplate) {
    reactiveMongoTemplate.dropCollection(Log.class).block();
    reactiveMongoTemplate
        .createCollection(
            Log.class, CollectionOptions.empty().maxDocuments(5).size(1024 * 1024L).capped())
        .block();
  }

  private void persistDocument(Log log) {
    repository.save(log).block();
  }

  @Test
  public void whenWarnLogsArePersisted_thenTheyAreReceivedByLogsCounter() throws Exception {
    WarnLogsCounter warnLogsCounter = new WarnLogsCounter(template);

    Thread.sleep(1000L); // wait for initialization

    Flux.range(0, 10)
        .map(
            i ->
                Log.builder()
                    .level(i > 5 ? LogLevel.WARN : LogLevel.INFO)
                    .service("some-service")
                    .message("some log message")
                    .build())
        .map(entity -> repository.save(entity).subscribe())
        .blockLast();

    Thread.sleep(1000L); // wait to receive all messages from the reactive mongodb driver

    assertThat(warnLogsCounter.count(), is(5));
    warnLogsCounter.close();
  }
}
