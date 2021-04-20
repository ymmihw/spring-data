package com.ymmihw.spring.data.neo4j.domain;

import java.util.Collection;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import lombok.Getter;
import lombok.Setter;

@RelationshipProperties
@Getter
@Setter
public class Role {
  @Id
  @GeneratedValue
  private Long id;
  private Collection<String> roles;
//  @TargetNode
//  private Person person;
  @TargetNode
  private Movie movie;

}
