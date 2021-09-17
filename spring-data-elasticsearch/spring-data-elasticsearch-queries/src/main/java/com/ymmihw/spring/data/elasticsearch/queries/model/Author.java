package com.ymmihw.spring.data.elasticsearch.queries.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author {

  private String name;

  public Author(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Author{" + "name='" + name + '\'' + '}';
  }
}
