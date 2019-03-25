package com.ymmihw.spring.data.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.ymmihw.spring.data.mongodb.model.Account;

public interface AccountMongoRepository extends ReactiveMongoRepository<Account, String> {
}
