package com.ymmihw.spring.data.mongodb.pa.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import com.ymmihw.spring.data.mongodb.pa.BaseTest;
import com.ymmihw.spring.data.mongodb.pa.model.User;

public class UserRepositoryProjectionLiveTest extends BaseTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MongoOperations mongoOps;

  @BeforeEach
  public void testSetup() {
    if (!mongoOps.collectionExists(User.class)) {
      mongoOps.createCollection(User.class);
    }
  }

  @AfterEach
  public void tearDown() {
    mongoOps.dropCollection(User.class);
  }

  @Test
  public void givenUserExists_whenAgeZero_thenSuccess() {
    mongoOps.insert(new User("John", 30));
    mongoOps.insert(new User("Ringo", 35));

    userRepository.findNameAndId().forEach(user -> {
      assertNotNull(user.getName());
      assertNull(user.getAge());
    });
  }

  @Test
  public void givenUserExists_whenIdNull_thenSuccess() {
    mongoOps.insert(new User("John", 30));
    mongoOps.insert(new User("Ringo", 35));

    userRepository.findNameAndAgeExcludeId().forEach(user -> {
      assertNull(user.getId());
      assertNotNull(user.getAge());
    });
  }

}
