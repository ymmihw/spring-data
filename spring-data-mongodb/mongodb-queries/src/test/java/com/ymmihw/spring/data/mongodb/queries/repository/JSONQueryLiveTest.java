package com.ymmihw.spring.data.mongodb.queries.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.ymmihw.spring.data.mongodb.queries.model.User;

public class JSONQueryLiveTest extends BaseQueryLiveTest {

  @Test
  public void givenUsersExist_whenFindingUsersByName_thenUsersAreFound() {
    User user = new User();
    user.setName("Eric");
    user.setAge(45);
    mongoOps.insert(user);
    user = new User();
    user.setName("Antony");
    user.setAge(55);
    mongoOps.insert(user);

    List<User> users = userRepository.findUsersByName("Eric");
    assertThat(users.size(), is(1));
  }

  @Test
  public void givenUsersExist_whenFindingUsersWithAgeCreaterThanAndLessThan_thenUsersAreFound() {
    User user = new User();
    user.setAge(20);
    user.setName("Jon");
    mongoOps.insert(user);

    user = new User();
    user.setAge(50);
    user.setName("Jon");
    mongoOps.insert(user);

    user = new User();
    user.setAge(33);
    user.setName("Jim");
    mongoOps.insert(user);

    List<User> users = userRepository.findUsersByAgeBetween(26, 40);
    assertThat(users.size(), is(1));
  }

  @Test
  public void givenUsersExist_whenFindingUserWithNameStartWithA_thenUsersAreFound() {
    User user = new User();
    user.setName("Eric");
    user.setAge(45);
    mongoOps.insert(user);

    user = new User();
    user.setName("Antony");
    user.setAge(33);
    mongoOps.insert(user);

    user = new User();
    user.setName("Alice");
    user.setAge(35);
    mongoOps.insert(user);

    List<User> users = userRepository.findUsersByRegexpName("^A");

    assertThat(users.size(), is(2));
  }

  @Test
  public void givenUsersExist_whenFindingUserWithNameEndWithC_thenUsersAreFound() {
    User user = new User();
    user.setName("Eric");
    user.setAge(45);
    mongoOps.insert(user);

    user = new User();
    user.setName("Antony");
    user.setAge(33);
    mongoOps.insert(user);

    user = new User();
    user.setName("Alice");
    user.setAge(35);
    mongoOps.insert(user);

    List<User> users = userRepository.findUsersByRegexpName("c$");

    assertThat(users.size(), is(1));
  }
}
