package com.ymmihw.spring.data.mongodb.custom.cascading.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.MongoClient;
import com.ymmihw.spring.data.mongodb.custom.cascading.event.CascadeSaveMongoEventListener;

@Configuration
@EnableMongoRepositories(
    basePackages = "com.ymmihw.spring.data.mongodb.custom.cascading.repository")
public class SimpleMongoConfig {
  @Autowired
  private MongoClient mongoClient;

  @Bean
  public CascadeSaveMongoEventListener cascadingMongoEventListener() {
    return new CascadeSaveMongoEventListener();
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongoClient, "test");
  }

}
