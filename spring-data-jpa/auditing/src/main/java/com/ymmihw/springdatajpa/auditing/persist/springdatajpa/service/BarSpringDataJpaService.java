package com.ymmihw.springdatajpa.auditing.persist.springdatajpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.springdatajpa.auditing.model.Bar;
import com.ymmihw.springdatajpa.auditing.persist.springdatajpa.dao.IBarCrudRepository;

@Service
@Transactional
public class BarSpringDataJpaService implements IBarService {

  @Autowired
  private IBarCrudRepository dao;

  @Override
  public void create(Bar bar) {
    dao.save(bar);
  }

  @Override
  public Bar update(Bar bar) {
    return dao.save(bar);
  }

}
