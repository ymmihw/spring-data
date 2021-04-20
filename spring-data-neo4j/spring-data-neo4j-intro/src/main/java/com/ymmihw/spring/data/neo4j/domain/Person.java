package com.ymmihw.spring.data.neo4j.domain;

import java.util.List;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.Getter;
import lombok.Setter;

@Node
@Getter
@Setter
public class Person {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private int born;

  @Relationship(type = "ACTED_IN")
  private List<Movie> movies;

}
