package com.ymmihw.spring.data.elasticsearch.tags.model;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;
import static org.springframework.data.elasticsearch.annotations.FieldType.Nested;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Document(indexName = "blog", indexStoreType = "article")
public class Article {

  @Id
  private String id;

  @MultiField(mainField = @Field(type = Text),
      otherFields = {@InnerField(suffix = "verbatim", type = Keyword),
          @InnerField(suffix = "Text", type = Text, fielddata = true)})
  private String title;

  @Field(type = Nested, includeInParent = true)
  private List<Author> authors;

  @Field(type = Keyword)
  private String[] tags;

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

  public String[] getTags() {
    return tags;
  }

  public void setTags(String... tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    return "Article{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", authors=" + authors
        + ", tags=" + Arrays.toString(tags) + '}';
  }
}
