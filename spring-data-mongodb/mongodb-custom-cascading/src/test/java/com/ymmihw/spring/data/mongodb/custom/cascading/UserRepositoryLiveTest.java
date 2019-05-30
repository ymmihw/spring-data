package com.ymmihw.spring.data.mongodb.custom.cascading;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.custom.cascading.UserRepositoryLiveTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.custom.cascading.config.SimpleMongoConfig;
import com.ymmihw.spring.data.mongodb.custom.cascading.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.custom.cascading.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
public class UserRepositoryLiveTest {

  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      ServerAddress addr =
          new ServerAddress(container.getContainerIpAddress(), container.getFirstMappedPort());
      return new MongoClient(addr);
    }
  }

  @Autowired
  private MongoTemplate mongoTemplate;


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

  @Test
  public void whenSavingUserWithEmailAddress_thenUserandEmailAddressSaved() {
    final User user = new User();
    user.setName("Brendan");
    final EmailAddress emailAddress = new EmailAddress();
    emailAddress.setValue("b@gmail.com");
    user.setEmailAddress(emailAddress);
    mongoTemplate.insert(user);

    assertThat(mongoTemplate.findOne(Query.query(Criteria.where("name").is("Brendan")), User.class)
        .getEmailAddress().getValue(), is("b@gmail.com"));
  }


}
