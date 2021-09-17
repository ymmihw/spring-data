package com.ymmihw.spring.data.mongodb.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb.repository")
public class MongoReactiveConfig {

  @Autowired private MongoClient mongoClient;

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(mongoClient, "test");
  }
}
