package com.ymmihw.spring.data.mongodb.pa.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.mongodb.pa.config.SimpleMongoConfig;
import com.ymmihw.spring.data.mongodb.pa.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SimpleMongoConfig.class)
public class UserRepositoryProjectionLiveTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MongoOperations mongoOps;

  @Before
  public void testSetup() {
    if (!mongoOps.collectionExists(User.class)) {
      mongoOps.createCollection(User.class);
    }
  }

  @After
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
