package com.ymmihw.spring.data.neo4j.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
@Getter
@Setter
public class Person {
  @Id @GeneratedValue private Long id;

  private String name;
  private int born;

//  @Relationship(type = "ACTED_IN", direction = Relationship.Direction.OUTGOING)
//  private List<Role> roles;

  @Relationship(type = "ACTED_IN", direction = Relationship.Direction.OUTGOING)
  private List<Movie> movies;
}
