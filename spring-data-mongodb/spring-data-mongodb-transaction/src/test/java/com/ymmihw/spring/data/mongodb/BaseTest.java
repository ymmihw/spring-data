package com.ymmihw.spring.data.mongodb;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ymmihw.spring.data.mongodb.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoTransactionConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoClientDockerConfig.class, MongoTransactionConfig.class})
public class BaseTest {
  @ClassRule
  public static SessionMongoContainer container = new SessionMongoContainer();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      ServerAddress addr =
          new ServerAddress(container.getContainerIpAddress(), container.getFirstMappedPort());
      return new MongoClient(addr);
    }
  }

  @Test
  public void test() {

  }
}
