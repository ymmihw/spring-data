package com.ymmihw.spring.data.mongodb;

import com.ymmihw.spring.data.mongodb.model.User;
import com.ymmihw.spring.data.mongodb.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoTransactionalLiveTest extends BaseTest {

  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private UserRepository userRepository;

  @Test
  @Transactional
  @Order(1)
  public void whenPerformMongoTransaction_thenSuccess() {
    userRepository.save(new User("John", 30));
    userRepository.save(new User("Ringo", 35));
    Query query = new Query().addCriteria(Criteria.where("name").is("John"));
    List<User> users = mongoTemplate.find(query, User.class);

    assertThat(users.size(), is(1));
  }

  @Test
  @Transactional
  @Order(2)
  public void whenListCollectionDuringMongoTransaction_thenException() {
    assertThrows(
        MongoTransactionException.class,
        () -> {
          if (mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.save(new User("John", 30));
            mongoTemplate.save(new User("Ringo", 35));
          }
        });
  }

  @Test
  @Transactional
  @Order(3)
  public void whenCountDuringMongoTransaction_thenException() {
    userRepository.save(new User("John", 30));
    userRepository.save(new User("Ringo", 35));
    long count = userRepository.count();
    assertEquals(2, count);
  }

  @Test
  @Transactional
  @Order(4)
  public void whenQueryDuringMongoTransaction_thenSuccess() {
    userRepository.save(new User("Jane", 20));
    userRepository.save(new User("Nick", 33));
    List<User> users = mongoTemplate.find(new Query(), User.class);

    assertTrue(users.size() > 1);
  }

  // ==== Using test instead of before and after due to @transactional doesn't allow list collection

  @Test
  @Order(-1)
  public void setup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
  }

  @Test
  @Order(6)
  public void tearDown() {
    mongoTemplate.dropCollection(User.class);
  }
}
