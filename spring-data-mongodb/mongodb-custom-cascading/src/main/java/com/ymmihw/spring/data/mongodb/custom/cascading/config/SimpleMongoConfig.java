package com.ymmihw.spring.data.mongodb.custom.cascading.config;

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

  private MongoClient mongo() throws Exception {
    return new MongoClient("192.168.10.177");
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), "test");
  }

  @Bean
  public CascadeSaveMongoEventListener cascadingMongoEventListener() {
    return new CascadeSaveMongoEventListener();
  }

}
