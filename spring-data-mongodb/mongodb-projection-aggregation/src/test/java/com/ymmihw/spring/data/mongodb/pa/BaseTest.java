package com.ymmihw.spring.data.mongodb.pa;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.pa.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.pa.config.SimpleMongoConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
public class BaseTest {
  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      MongoClient client = MongoClients.create(
          "mongodb://" + container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
      return client;
    }
  }

  @Test
  public void test() {

  }
}
