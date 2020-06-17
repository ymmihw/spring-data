package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb")
public class MongoTransactionConfig extends AbstractMongoClientConfiguration {
  private final MongoClient mongoClient;

  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
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
