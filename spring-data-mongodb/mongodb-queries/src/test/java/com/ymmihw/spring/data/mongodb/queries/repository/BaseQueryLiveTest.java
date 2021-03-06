package com.ymmihw.spring.data.mongodb.queries.repository;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import com.ymmihw.spring.data.mongodb.queries.BaseTest;
import com.ymmihw.spring.data.mongodb.queries.model.User;

public class BaseQueryLiveTest extends BaseTest {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected MongoOperations mongoOps;

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
}
