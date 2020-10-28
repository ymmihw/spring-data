package com.ymmihw.spring.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Article {
  @Id
  @GeneratedValue
  private Long id;
  private String content;

  public Article() {}

  public Article(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Article{" + "id=" + id + ", content='" + content + '\'' + '}';
  }
}
