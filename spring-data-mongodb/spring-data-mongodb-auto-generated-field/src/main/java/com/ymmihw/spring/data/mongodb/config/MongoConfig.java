package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb")
@RequiredArgsConstructor
public class MongoConfig extends AbstractMongoConfiguration {
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
