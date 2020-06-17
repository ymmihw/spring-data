package com.ymmihw.spring.data.mongodb.iac;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.mongodb.iac.model.EmailAddress;
import com.ymmihw.spring.data.mongodb.iac.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class AnnotationTest extends BaseTest {
  @Test
  public void whenSavingUserWithoutSettingAge_thenAgeIsSetByDefault() {
    final User user = new User();
    user.setName("Alex");
    mongoTemplate.insert(user);

    assertThat(
        mongoTemplate.findOne(Query.query(Criteria.where("name").is("Alex")), User.class).getAge(),
        is(0));
  }

  @Test
  public void whenSavingUser_thenYearOfBirthIsCalculated() {
    final User user = new User();
    user.setName("Alex");
    user.setYearOfBirth(1985);
    mongoTemplate.insert(user);

    assertThat(mongoTemplate.findOne(Query.query(Criteria.where("name").is("Alex")), User.class)
        .getYearOfBirth(), is(nullValue()));
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
