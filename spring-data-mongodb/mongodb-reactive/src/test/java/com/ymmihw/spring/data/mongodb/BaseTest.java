package com.ymmihw.spring.data.mongodb;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.BaseTest.MongoClientDockerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(classes = {Spring5ReactiveApplication.class, MongoClientDockerConfig.class})
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class BaseTest {
  @Container public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  @Profile({"test"})
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() {
      return MongoClients.create(
          "mongodb://" + container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
    }
  }

  @Test
  public void test() {}
}
