package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@EnableReactiveMongoRepositories
public class MongoReactiveApplication extends AbstractReactiveMongoConfiguration {
  @Override
  protected String getDatabaseName() {
    return "reactive";
  }

  @Override
  @Bean
  public MongoClient reactiveMongoClient() {
    return MongoClients.create();
  }
}
