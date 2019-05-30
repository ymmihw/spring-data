package com.ymmihw.spring.data.mongodb.introduction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.MongoClient;

@Configuration
public class MongoClientConfig {
  @Bean
  public MongoClient mongo() throws Exception {
    return new MongoClient("192.168.10.177");
  }

}
