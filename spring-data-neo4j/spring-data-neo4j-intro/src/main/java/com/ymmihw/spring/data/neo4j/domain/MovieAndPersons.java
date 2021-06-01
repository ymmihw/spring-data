package com.ymmihw.spring.data.neo4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovieAndPersons {
  private Movie movie;
  private List<Person> person;
}
