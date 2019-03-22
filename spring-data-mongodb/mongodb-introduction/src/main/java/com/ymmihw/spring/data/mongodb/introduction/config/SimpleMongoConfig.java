package com.ymmihw.spring.data.mongodb.introduction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb.introduction.repository")
public class SimpleMongoConfig {

  private MongoClient mongo() throws Exception {
    return new MongoClient("192.168.10.177");
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), "test");
  }

}
