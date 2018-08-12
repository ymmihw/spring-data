package com.ymmihw.spring.data.mongodb.introduction.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.spring.data.mongodb.introduction.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
