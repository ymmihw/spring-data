package com.ymmihw.spring.data.mongodb.queries;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.queries.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.queries.config.SimpleMongoConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
public class BaseTest {
  @Container public static MongoContainer container = MongoContainer.getInstance();

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
