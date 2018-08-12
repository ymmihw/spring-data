package com.ymmihw.spring.data.mongodb.pa.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.ymmihw.spring.data.mongodb.pa.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  @Query(value = "{}", fields = "{name : 1}")
  List<User> findNameAndId();

  @Query(value = "{}", fields = "{_id : 0}")
  List<User> findNameAndAgeExcludeId();
}
