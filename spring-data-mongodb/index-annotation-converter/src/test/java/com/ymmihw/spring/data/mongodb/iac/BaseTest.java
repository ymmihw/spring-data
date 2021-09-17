package com.ymmihw.spring.data.mongodb.iac;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.iac.BaseTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.iac.config.MongoConfig;
import com.ymmihw.spring.data.mongodb.iac.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.iac.model.User;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ContextConfiguration(
    classes = {MongoClientDockerConfig.class, MongoConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Testcontainers
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

  @Autowired protected MongoTemplate mongoTemplate;

  @BeforeEach
  public void testSetup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
    if (!mongoTemplate.collectionExists(EmailAddress.class)) {
      mongoTemplate.createCollection(EmailAddress.class);
    }
  }

  @AfterEach
  public void tearDown() {
    mongoTemplate.dropCollection(User.class);
    mongoTemplate.dropCollection(EmailAddress.class);
  }
}
