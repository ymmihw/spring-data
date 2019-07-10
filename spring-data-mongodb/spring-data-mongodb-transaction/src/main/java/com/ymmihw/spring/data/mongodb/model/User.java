package com.ymmihw.spring.data.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
@CompoundIndexes({@CompoundIndex(name = "email_age", def = "{'email.id' : 1, 'age': 1}")})
public class User {

  @Id
  private String id;
  @Indexed(direction = IndexDirection.ASCENDING)
  private String name;
  @Indexed(direction = IndexDirection.ASCENDING)
  private Integer age;
  @Transient
  private Integer yearOfBirth;

  public User(String name, Integer age) {
    super();
    this.name = name;
    this.age = age;
  }

}
