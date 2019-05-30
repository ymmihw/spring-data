package com.ymmihw.spring.data.mongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class MongoMongoReactiveClientConfig {
  @Bean
  public MongoClient reactiveMongoClient() {
    return MongoClients.create("mongodb://192.168.10.177");
  }

}
