package com.ymmihw.spring.model;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Document("articles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Article {

  @Id private String id;

  @ArangoId private String arangoId;

  private String name;
  private String author;
  private ZonedDateTime publishDate;
  private String htmlContent;

  @Relations(edges = ArticleLink.class, lazy = true)
  private Collection<Author> authors;

  public Article(String name, String author, ZonedDateTime publishDate, String htmlContent) {
    this.name = name;
    this.author = author;
    this.publishDate = publishDate;
    this.htmlContent = htmlContent;
  }
}
