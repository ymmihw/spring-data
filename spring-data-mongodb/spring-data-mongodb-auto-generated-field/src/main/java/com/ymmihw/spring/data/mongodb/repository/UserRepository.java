package com.ymmihw.spring.data.mongodb.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.spring.data.mongodb.model.User;

public interface UserRepository extends MongoRepository<User, Long> {

}
