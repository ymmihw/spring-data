package com.ymmihw.spring.data.mongodb.repository;

import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import reactor.core.publisher.Flux;

public interface LogsRepository extends ReactiveCrudRepository<Log, String> {
  @Tailable
  Flux<Log> findByLevel(LogLevel level);
}
