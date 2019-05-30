package com.ymmihw.spring.data.mongodb.queries;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.queries.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.queries.config.SimpleMongoConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
public class BaseTest {
  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

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
