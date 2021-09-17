package com.ymmihw.spring.data.elasticsearch.tags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Author {

  private String name;

  @Override
  public String toString() {
    return "Author{" + "name='" + name + '\'' + '}';
  }
}
