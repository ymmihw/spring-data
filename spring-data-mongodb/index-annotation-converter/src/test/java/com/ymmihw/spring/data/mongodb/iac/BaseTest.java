package com.ymmihw.spring.data.mongodb.iac;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import com.ymmihw.spring.data.mongodb.iac.config.MongoConfig;
import com.ymmihw.spring.data.mongodb.iac.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.iac.model.User;

@ContextConfiguration(classes = MongoConfig.class)
public class BaseTest {

  @Autowired
  protected MongoTemplate mongoTemplate;


  @Before
  public void testSetup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
    if (!mongoTemplate.collectionExists(EmailAddress.class)) {
      mongoTemplate.createCollection(EmailAddress.class);
    }
  }

  @After
  public void tearDown() {
    mongoTemplate.dropCollection(User.class);
    mongoTemplate.dropCollection(EmailAddress.class);
  }


}
