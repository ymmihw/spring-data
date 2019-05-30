package com.ymmihw.spring.data.mongodb.iac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.MongoClient;

@Configuration
public class MongoClientConfig {
  @Bean
  public MongoClient mongoClient() {
    return new MongoClient("192.168.10.177");
  }
}
