package com.ymmihw.spring.data.mongodb;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.BaseTest.MongoClientDockerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = {LogsCounterApplication.class, MongoClientDockerConfig.class})
public class BaseTest {
  @Container public static final MongoContainer CONTAINER = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() {
      return MongoClients.create(
          "mongodb://" + CONTAINER.getContainerIpAddress() + ":" + CONTAINER.getFirstMappedPort());
    }
  }

  @Test
  public void test() {}
}
