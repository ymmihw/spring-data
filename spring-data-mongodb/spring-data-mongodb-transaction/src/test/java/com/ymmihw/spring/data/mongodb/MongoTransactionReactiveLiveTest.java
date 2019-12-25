package com.ymmihw.spring.data.mongodb;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.reactive.TransactionalOperator;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoTransactionReactiveLiveTest.ReactiveMongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.config.MongoReactiveConfig;
import com.ymmihw.spring.data.mongodb.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritInitializers = false, classes = {MongoReactiveConfig.class,
    ReactiveMongoClientDockerConfig.class, TransactionAutoConfiguration.class})
public class MongoTransactionReactiveLiveTest {
  @ClassRule
  public static MongoContainer container = new SessionMongoContainer();

  @Configuration
  public static class ReactiveMongoClientDockerConfig {
    @Bean
    public MongoClient reactiveMongoClient() throws InterruptedException {
      container.start();
      TimeUnit.SECONDS.sleep(10);
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

  @Autowired
  private TransactionalOperator transactionalOperator;

  @Test
  public void whenPerformTransaction_thenSuccess() {
    User user1 = new User("Jane", 23);
    User user2 = new User("John", 34);
    transactionalOperator
        .execute(reactiveTransaction -> reactiveOps.insert(user1).then(reactiveOps.insert(user2)));
  }

}
