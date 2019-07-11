package com.ymmihw.spring.data.mongodb;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import com.ymmihw.spring.data.mongodb.repository.LogsRepository;
import com.ymmihw.spring.data.mongodb.service.InfoLogsCounter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;


@Slf4j
public class InfoLogsCounterManualTest extends BaseTest {

  @Autowired
  private LogsRepository repository;

  @Autowired
  private ReactiveMongoTemplate template;


  @Before
  public void setUp() {
    createCappedCollectionUsingReactiveMongoTemplate(template);

    persistDocument(Log.builder().level(LogLevel.INFO).service("Service 2")
        .message("Initial INFO message").build());
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
  public void wheInfoLogsArePersisted_thenTheyAreReceivedByLogsCounter() throws Exception {
    InfoLogsCounter infoLogsCounter = new InfoLogsCounter(repository);

    Thread.sleep(1000L); // wait for initialization

    Flux.range(0, 10)
        .map(i -> Log.builder().level(i > 5 ? LogLevel.WARN : LogLevel.INFO).service("some-service")
            .message("some log message").build())
        .map(entity -> repository.save(entity).subscribe()).blockLast();

    Thread.sleep(1000L); // wait to receive all messages from the reactive mongodb driver

    assertThat(infoLogsCounter.count(), is(7));
    infoLogsCounter.close();
  }
}
