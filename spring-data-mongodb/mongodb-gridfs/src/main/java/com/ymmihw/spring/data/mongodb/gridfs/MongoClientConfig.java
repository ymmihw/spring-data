package com.ymmihw.spring.data.mongodb.gridfs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoClientConfig {
  @Bean
  public MongoClient mongo() throws Exception {
    MongoClient client = MongoClients.create("mongodb://192.168.10.177:27017");
    return client;
  }
}
