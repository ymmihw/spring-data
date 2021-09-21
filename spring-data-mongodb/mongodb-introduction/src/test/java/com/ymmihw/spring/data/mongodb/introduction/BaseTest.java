package com.ymmihw.spring.data.mongodb.introduction;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.introduction.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.introduction.config.SimpleMongoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
@SpringBootTest
@Testcontainers
public class BaseTest {
  @Container
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      MongoClient client =
          MongoClients.create(
              "mongodb://"
                  + container.getContainerIpAddress()
                  + ":"
                  + container.getFirstMappedPort());
      return client;
    }
  }

  @Test
  public void test() {}
}
