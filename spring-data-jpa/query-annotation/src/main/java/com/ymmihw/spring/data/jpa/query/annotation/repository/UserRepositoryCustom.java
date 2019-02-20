package com.ymmihw.spring.data.jpa.query.annotation.repository;

import java.util.List;
import java.util.Set;
import com.ymmihw.spring.data.jpa.query.annotation.model.User;

public interface UserRepositoryCustom {
  List<User> findUserByEmails(Set<String> emails);
}
