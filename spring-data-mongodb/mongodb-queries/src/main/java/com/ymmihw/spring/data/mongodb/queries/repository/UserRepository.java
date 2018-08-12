package com.ymmihw.spring.data.mongodb.queries.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.ymmihw.spring.data.mongodb.queries.model.User;

public interface UserRepository
    extends MongoRepository<User, String>, QuerydslPredicateExecutor<User> {

  List<User> findByName(String string);

  List<User> findByAgeBetween(int i, int j);

  List<User> findByNameStartingWith(String string);

  List<User> findByNameEndingWith(String string);

  List<User> findByNameLikeOrderByAgeAsc(String string);

  @Query("{ 'name' : ?0 }")
  List<User> findUsersByName(String string);

  @Query("{ 'age' : { $gt: ?0, $lt: ?1 } }")
  List<User> findUsersByAgeBetween(int i, int j);

  @Query("{ 'name' : { $regex: ?0 } }")
  List<User> findUsersByRegexpName(String string);
}
