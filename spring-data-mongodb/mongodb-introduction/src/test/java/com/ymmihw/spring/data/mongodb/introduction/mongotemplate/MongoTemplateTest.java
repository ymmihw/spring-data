package com.ymmihw.spring.data.mongodb.introduction.mongotemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.mongodb.introduction.config.SimpleMongoConfig;
import com.ymmihw.spring.data.mongodb.introduction.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SimpleMongoConfig.class)
public class MongoTemplateTest {
  @Autowired
  private MongoOperations mongoOps;

  @Autowired
  private MongoTemplate mongoTemplate;

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

  // insert
  @Test
  public void whenInsertingUser_thenUserIsInserted() {
    final User user = new User();
    user.setName("Jon");
    mongoTemplate.insert(user);

    Query query = Query.query(Criteria.where("name").is("Jon"));
    assertThat(mongoOps.findOne(query, User.class).getName(), is("Jon"));
  }

  // save - insert
  @Test
  public void whenSavingNewUser_thenUserIsInserted() {
    final User user = new User();
    user.setName("Albert");
    mongoTemplate.save(user);

    Query query = Query.query(Criteria.where("name").is("Albert"));
    assertThat(mongoOps.findOne(query, User.class).getName(), is("Albert"));
  }

  // save - update
  @Test
  public void givenUserExists_whenSavingExistUser_thenUserIsUpdated() {
    User user = new User();
    user.setName("Jack");
    mongoOps.insert(user);

    user = mongoOps.findOne(Query.query(Criteria.where("name").is("Jack")), User.class);

    user.setName("Jim");
    mongoTemplate.save(user);

    assertThat(mongoOps.findAll(User.class).size(), is(1));
  }

  // save - update
  @Test
  public void givenMultiUserExists_whenUpdateFirst_thenFirstUserIsUpdated() {
    User user1 = new User();
    user1.setName("Alex");
    mongoOps.insert(user1);

    User user2 = new User();
    user2.setName("Alex");
    mongoOps.insert(user2);

    Query query = new Query();
    query.addCriteria(Criteria.where("name").is("Alex"));
    Update update = new Update();
    update.set("name", "James");
    mongoTemplate.updateFirst(query, update, User.class);

    assertThat(mongoOps.findById(user1.getId(), User.class).getName(), is("James"));
  }

  // UpdateMulti
  @Test
  public void givenMultiUserExists_whenUpdateMulti_thenMultiUserIsUpdated() {
    {
      User user1 = new User();
      user1.setName("Alex");
      mongoOps.insert(user1);

      User user2 = new User();
      user2.setName("Alex");
      mongoOps.insert(user2);
    }
    {
      Query query = new Query();
      query.addCriteria(Criteria.where("name").is("Alex"));
      Update update = new Update();
      update.set("name", "James");
      mongoTemplate.updateMulti(query, update, User.class);
    }
    {
      Query query = new Query();
      query.addCriteria(Criteria.where("name").is("James"));
      assertThat(mongoOps.find(query, User.class).size(), is(2));
    }
  }

  // FindAndModify
  @Test
  public void givenUserExists_whenFindAndModify_thenUserIsUpdated() {
    User user1 = new User();
    user1.setName("Markus");
    mongoOps.insert(user1);

    Query query = new Query();
    query.addCriteria(Criteria.where("name").is("Markus"));
    Update update = new Update();
    update.set("name", "Nick");
    User user2 = mongoTemplate.findAndModify(query, update, User.class);

    assertThat(user2.getName(), is("Markus"));
    Query query2 = new Query();
    query2.addCriteria(Criteria.where("name").is("Nick"));
    assertThat(mongoTemplate.findOne(query2, User.class).getId(), is(user2.getId()));
  }

  // FindAndModify
  @Test
  public void givenUserExists_whenUpsert_thenUserIsUpdated() {
    User user = new User();
    user.setName("Markus");
    mongoOps.insert(user);

    Query query = new Query();
    query.addCriteria(Criteria.where("name").is("Markus"));
    Update update = new Update();
    update.set("name", "Nick");
    mongoTemplate.upsert(query, update, User.class);

    Query query2 = new Query();
    query2.addCriteria(Criteria.where("name").is("Nick"));
    assertThat(mongoTemplate.findOne(query2, User.class).getId(), is(user.getId()));
  }

  // remove
  @Test
  public void givenUserExists_whenRemove_thenUserIsDeleted() {
    final User user = new User();
    user.setName("Benn");
    mongoOps.insert(user);

    mongoTemplate.remove(user);

    Query query = Query.query(Criteria.where("name").is("Benn"));
    assertThat(mongoOps.find(query, User.class).size(), is(0));
  }
}
