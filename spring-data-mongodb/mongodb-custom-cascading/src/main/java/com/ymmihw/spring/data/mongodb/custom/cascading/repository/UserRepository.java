package com.ymmihw.spring.data.mongodb.custom.cascading.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.spring.data.mongodb.custom.cascading.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
