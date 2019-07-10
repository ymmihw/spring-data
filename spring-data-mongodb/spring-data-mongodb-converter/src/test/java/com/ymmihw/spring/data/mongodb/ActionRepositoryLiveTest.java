package com.ymmihw.spring.data.mongodb;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ymmihw.spring.data.mongodb.ActionRepositoryLiveTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoConfig;
import com.ymmihw.spring.data.mongodb.model.Action;
import com.ymmihw.spring.data.mongodb.repository.ActionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoClientDockerConfig.class, MongoConfig.class})
public class ActionRepositoryLiveTest {
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

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ActionRepository actionRepository;

  @Before
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

  @After
  public void tearDown() {
    mongoTemplate.dropCollection(Action.class);
  }
}
