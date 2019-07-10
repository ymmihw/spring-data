package com.ymmihw.spring.data.mongodb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ContextConfiguration;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoTransactionReactiveLiveTest.ReactiveMongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoReactiveConfig;
import com.ymmihw.spring.data.mongodb.model.User;

@ContextConfiguration(inheritInitializers = false,
    classes = {MongoReactiveConfig.class, ReactiveMongoClientDockerConfig.class})
public class MongoTransactionReactiveLiveTest extends BaseTest {
  @Configuration
  public static class ReactiveMongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() {
      return MongoClients.create(
          "mongodb://" + container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
    }
  }

  @Autowired
  private ReactiveMongoOperations reactiveOps;

  @Before
  public void testSetup() {
    if (!reactiveOps.collectionExists(User.class).block()) {
      reactiveOps.createCollection(User.class);
    }
  }

  @After
  public void tearDown() {
    System.out.println(reactiveOps.findAll(User.class).count().block());
    reactiveOps.dropCollection(User.class);
  }

  @Test
  public void whenPerformTransaction_thenSuccess() {
    User user1 = new User("Jane", 23);
    User user2 = new User("John", 34);
    reactiveOps.inTransaction().execute(action -> action.insert(user1).then(action.insert(user2)));
  }

}
