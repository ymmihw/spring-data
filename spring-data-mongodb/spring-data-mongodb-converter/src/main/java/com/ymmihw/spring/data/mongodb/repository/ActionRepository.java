package com.ymmihw.spring.data.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ymmihw.spring.data.mongodb.model.Action;

public interface ActionRepository extends MongoRepository<Action, String> {
}
