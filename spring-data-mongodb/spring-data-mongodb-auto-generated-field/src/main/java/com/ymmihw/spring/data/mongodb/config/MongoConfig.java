package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb")
@RequiredArgsConstructor
public class MongoConfig extends AbstractMongoClientConfiguration {
  private final MongoClient mongoClient;

  @Override
  public MongoClient mongoClient() {
    return mongoClient;
  }

  @Override
  protected String getDatabaseName() {
    return "test";
  }
}
