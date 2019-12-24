package com.ymmihw.spring.data.mongodb.introduction.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.ymmihw.spring.data.mongodb.introduction.BaseTest;
import com.ymmihw.spring.data.mongodb.introduction.model.User;

public class UserRepositoryLiveTest extends BaseTest {
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

  // insert
  @Test
  public void whenInsertingUser_thenUserIsInserted() {
    final User user = new User();
    user.setName("Jon");
    userRepository.insert(user);

    Query query = Query.query(Criteria.where("name").is("Jon"));
    assertThat(mongoOps.findOne(query, User.class).getName(), is("Jon"));
  }

  // save - insert
  @Test
  public void whenSavingNewUser_thenUserIsInserted() {
    final User user = new User();
    user.setName("Albert");
    userRepository.save(user);

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
    userRepository.save(user);

    assertThat(mongoOps.findAll(User.class).size(), is(1));
  }

  // delete
  @Test
  public void givenUserExists_whenDeletingUser_thenUserIsDeleted() {
    final User user = new User();
    user.setName("Benn");
    mongoOps.insert(user);

    userRepository.delete(user);

    Query query = Query.query(Criteria.where("name").is("Benn"));
    assertThat(mongoOps.find(query, User.class).size(), is(0));
  }

  // findOne
  @Test
  public void givenUserExists_whenFindingUser_thenUserIsFound() {
    User user = new User();
    user.setName("Chris");
    mongoOps.insert(user);

    Query query = Query.query(Criteria.where("name").is("Chris"));
    user = mongoOps.findOne(query, User.class);
    final Optional<User> foundUser = userRepository.findById(user.getId());

    assertThat(user.getName(), is(foundUser.get().getName()));
  }

  // exists
  @Test
  public void givenUserExists_whenCheckingDoesUserExist_thenUserIsExist() {
    User user = new User();
    user.setName("Harris");
    mongoOps.insert(user);

    Query query = Query.query(Criteria.where("name").is("Harris"));
    user = mongoOps.findOne(query, User.class);
    final boolean isExists = userRepository.existsById(user.getId());

    assertThat(isExists, is(true));
  }

  // FindAll with Sort
  @Test
  public void givenUsersExist_whenFindingAllUsersWithSorting_thenUsersAreFoundAndSorted() {
    User user = new User();
    user.setName("Brendan");
    mongoOps.insert(user);

    user = new User();
    user.setName("Adam");
    mongoOps.insert(user);

    final List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));

    assertThat(users.size(), is(2));
    assertThat(users.get(0).getName(), is("Adam"));
    assertThat(users.get(1).getName(), is("Brendan"));
  }

  // FindAll with Pageable
  @Test
  public void givenUsersExist_whenFindingAllUsersWithPagination_thenUsersAreFoundAndOrderedOnPage() {
    User user = new User();
    user.setName("Brendan");
    mongoOps.insert(user);

    user = new User();
    user.setName("Adam");
    mongoOps.insert(user);

    final Pageable pageableRequest = PageRequest.of(0, 1);

    final Page<User> page = userRepository.findAll(pageableRequest);
    List<User> users = page.getContent();

    assertThat(users.size(), is(1));
    assertThat(page.getTotalPages(), is(2));
  }

}
