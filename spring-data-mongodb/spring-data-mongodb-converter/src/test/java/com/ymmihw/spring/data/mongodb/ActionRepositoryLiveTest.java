package com.ymmihw.spring.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.ActionRepositoryLiveTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoConfig;
import com.ymmihw.spring.data.mongodb.model.Action;
import com.ymmihw.spring.data.mongodb.repository.ActionRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {MongoClientDockerConfig.class, MongoConfig.class})
public class ActionRepositoryLiveTest {
  @Container
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

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ActionRepository actionRepository;

  @BeforeEach
  public void setup() {
    if (!mongoTemplate.collectionExists(Action.class)) {
      mongoTemplate.createCollection(Action.class);
    }
  }

  @Test
  public void givenSavedAction_TimeIsRetrievedCorrectly() {
    String id = "testId";
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

    actionRepository.save(new Action(id, "click-action", now));
    Action savedAction = actionRepository.findById(id).get();

    Assert.assertEquals(now.withNano(0), savedAction.getTime().withNano(0));
  }

  @AfterEach
  public void tearDown() {
    mongoTemplate.dropCollection(Action.class);
  }
}
