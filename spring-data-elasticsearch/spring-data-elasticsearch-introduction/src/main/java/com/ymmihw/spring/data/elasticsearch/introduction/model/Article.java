package com.ymmihw.spring.data.elasticsearch.introduction.model;

import java.util.List;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "blog", indexStoreType = "article")
public class Article {
  @Id
  private String id;

  private String title;

  @Field(type = Nested, includeInParent = true)
  private List<Author> authors;

  public Article() {}

  public Article(String title) {
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Author> authors) {
    this.authors = authors;
  }

  @Override
  public String toString() {
    return "Article{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", authors=" + authors
        + ", tags=" + '}';
  }
}
