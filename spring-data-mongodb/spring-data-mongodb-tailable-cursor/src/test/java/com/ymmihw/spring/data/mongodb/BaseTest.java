package com.ymmihw.spring.data.mongodb;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.BaseTest.MongoClientDockerConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LogsCounterApplication.class, MongoClientDockerConfig.class})
public class BaseTest {
  @ClassRule
  public static final MongoContainer CONTAINER = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() {
      CONTAINER.start();
      return MongoClients.create(
          "mongodb://" + CONTAINER.getContainerIpAddress() + ":" + CONTAINER.getFirstMappedPort());
    }
  }

  @Test
  public void test() {

  }
}
