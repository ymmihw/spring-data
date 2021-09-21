package com.ymmihw.spring.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoTransactionConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {MongoClientDockerConfig.class, MongoTransactionConfig.class})
public class BaseTest {
  @Container public static MongoContainer container = new SessionMongoContainer();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      container.start();
      TimeUnit.SECONDS.sleep(10);
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
