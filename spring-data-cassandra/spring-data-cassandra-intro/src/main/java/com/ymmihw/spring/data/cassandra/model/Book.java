package com.ymmihw.spring.data.cassandra.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table
@Getter
@Setter
@AllArgsConstructor
public class Book {

  @PrimaryKey
  private BookKey key;

  @Column
  private Set<String> tags = new HashSet<>();

  public UUID getId() {
    return key.getId();
  }

  public void setTitle(String title) {
    key.setTitle(title);
  }

  public String getTitle() {
    return key.getTitle();
  }
}
