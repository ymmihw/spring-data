package com.ymmihw.spring.model;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Document("articles")
@NoArgsConstructor
@Data
public class Author {

  @Id private String id;

  @ArangoId private String arangoId;

  private String name;
}
