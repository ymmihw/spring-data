package com.ymmihw.spring.data.mongodb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import com.mongodb.reactivestreams.client.MongoClient;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb.repository")
public class MongoReactiveConfig {

  @Autowired
  private MongoClient mongoClient;

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(mongoClient, "test");
  }


  @Bean
  ReactiveTransactionManager reactiveTransactionManager() {
    ReactiveMongoDatabaseFactory dbFactory =
        new SimpleReactiveMongoDatabaseFactory(mongoClient, "database");
    return new ReactiveMongoTransactionManager(dbFactory);
  }
}

