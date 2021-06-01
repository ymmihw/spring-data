package com.ymmihw.springdatajpa.auditing.persist.springdatajpa.service;

import com.ymmihw.springdatajpa.auditing.model.Bar;

public interface IBarService {

  void create(Bar bar);

  Bar update(Bar bar);
}
