package com.ymmihw.springdatajpa.auditing.persist.springdatajpa.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import com.ymmihw.springdatajpa.auditing.model.Bar;

public interface IBarCrudRepository extends CrudRepository<Bar, Serializable> {
}
