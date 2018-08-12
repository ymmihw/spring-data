package com.ymmihw.spring.data.mongodb.iac.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.spring.data.mongodb.iac.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
