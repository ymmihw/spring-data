package com.ymmihw.spring.data.neo4j.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@Node
@Getter
@Setter
public class Movie {
  @Id @GeneratedValue private Long id;

  private String title;

  private int released;
  private String tagline;

  private List<Person> persons = new ArrayList<>();
}
