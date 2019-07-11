package com.ymmihw.spring.data.mongodb.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PreDestroy;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Slf4j
public class WarnLogsCounter implements LogsCounter {

  private static final String LEVEL_FIELD_NAME = "level";

  private final AtomicInteger counter = new AtomicInteger();
  private final Disposable subscription;

  public WarnLogsCounter(ReactiveMongoOperations template) {
    Flux<Log> stream = template.tail(query(where(LEVEL_FIELD_NAME).is(LogLevel.WARN)), Log.class);
    subscription = stream.subscribe(logEntity -> {
      log.warn("WARN log received: " + logEntity);
      counter.incrementAndGet();
    });
  }

  @Override
  public int count() {
    return counter.get();
  }

  @PreDestroy
  public void close() {
    subscription.dispose();
  }
}
