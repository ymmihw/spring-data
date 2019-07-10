package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb")
public class MongoTransactionConfig extends AbstractMongoConfiguration {
  private final MongoClient mongoClient;

  @Bean
  MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Override
  protected String getDatabaseName() {
    return "test";
  }

  @Override
  public MongoClient mongoClient() {
    return mongoClient;
  }

}
