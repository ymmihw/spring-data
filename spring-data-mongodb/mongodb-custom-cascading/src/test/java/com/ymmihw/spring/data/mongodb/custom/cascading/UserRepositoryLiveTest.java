package com.ymmihw.spring.data.mongodb.custom.cascading;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.custom.cascading.UserRepositoryLiveTest.MongoClientDockerConfig;
import com.ymmihw.spring.data.mongodb.custom.cascading.config.SimpleMongoConfig;
import com.ymmihw.spring.data.mongodb.custom.cascading.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.custom.cascading.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(classes = {SimpleMongoConfig.class, MongoClientDockerConfig.class})
@SpringBootTest
@Testcontainers
public class UserRepositoryLiveTest {

  @Container public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      MongoClient client =
          MongoClients.create(
              "mongodb://"
                  + container.getContainerIpAddress()
                  + ":"
                  + container.getFirstMappedPort());
      return client;
    }
  }

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  public void testSetup() {
    if (!mongoTemplate.collectionExists(User.class)) {
      mongoTemplate.createCollection(User.class);
    }
    if (!mongoTemplate.collectionExists(EmailAddress.class)) {
      mongoTemplate.createCollection(EmailAddress.class);
    }
  }

  @AfterEach
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

    assertThat(
        mongoTemplate
            .findOne(Query.query(Criteria.where("name").is("Brendan")), User.class)
            .getEmailAddress()
            .getValue(),
        is("b@gmail.com"));
  }
}
