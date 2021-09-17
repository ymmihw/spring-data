package com.ymmihw.spring.data.elasticsearch.introduction.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;

@NoArgsConstructor
@Getter
@Setter
@Document(indexName = "article")
public class Article {
  @Id private String id;

  private String title;

  @Field(type = Nested, includeInParent = true)
  private List<Author> authors;

  public Article(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "Article{"
        + "id='"
        + id
        + '\''
        + ", title='"
        + title
        + '\''
        + ", authors="
        + authors
        + ", tags="
        + '}';
  }
}
