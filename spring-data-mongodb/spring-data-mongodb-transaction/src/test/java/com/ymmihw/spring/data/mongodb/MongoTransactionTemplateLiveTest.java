package com.ymmihw.spring.data.mongodb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import com.ymmihw.spring.data.mongodb.model.User;

public class MongoTransactionTemplateLiveTest extends BaseTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private MongoTransactionManager mongoTransactionManager;

  @BeforeEach
  public void testSetup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
  }

  @AfterEach
  public void tearDown() {
    mongoTemplate.dropCollection(User.class);
  }

  @Test
  public void givenTransactionTemplate_whenPerformTransaction_thenSuccess() {
    mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
    TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        mongoTemplate.insert(new User("Kim", 20));
        mongoTemplate.insert(new User("Jack", 45));
      };
    });

    Query query = new Query().addCriteria(Criteria.where("name").is("Jack"));
    List<User> users = mongoTemplate.find(query, User.class);

    assertThat(users.size(), is(1));
  }

}
