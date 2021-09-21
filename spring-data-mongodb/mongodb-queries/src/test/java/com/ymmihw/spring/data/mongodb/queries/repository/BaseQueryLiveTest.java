package com.ymmihw.spring.data.mongodb.queries.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import com.ymmihw.spring.data.mongodb.queries.BaseTest;
import com.ymmihw.spring.data.mongodb.queries.model.User;

public class BaseQueryLiveTest extends BaseTest {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected MongoOperations mongoOps;

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
}
