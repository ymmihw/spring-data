package com.ymmihw.spring.data.mongodb.iac;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.iac.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.iac.config.MongoConfig;
import com.ymmihw.spring.data.mongodb.iac.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.iac.model.User;

@ContextConfiguration(classes = {MongoClientDockerConfig.class, MongoConfig.class})
public class BaseTest {
  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongoClient() {
      ServerAddress addr =
          new ServerAddress(container.getContainerIpAddress(), container.getFirstMappedPort());
      return new MongoClient(addr);
    }
  }

  @Autowired
  protected MongoTemplate mongoTemplate;

  @Before
  public void testSetup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
    if (!mongoTemplate.collectionExists(EmailAddress.class)) {
      mongoTemplate.createCollection(EmailAddress.class);
    }
  }

  @After
  public void tearDown() {
    mongoTemplate.dropCollection(User.class);
    mongoTemplate.dropCollection(EmailAddress.class);
  }
}
