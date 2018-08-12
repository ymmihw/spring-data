package com.ymmihw.spring.data.mongodb.queries.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
@Document
public class User {

  @Id
  private String id;
  private String name;
  private Integer age;

  public User() {}

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
