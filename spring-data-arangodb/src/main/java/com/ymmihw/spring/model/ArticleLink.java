package com.ymmihw.spring.model;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Edge
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleLink {

  @From private Article article;

  @To private Author author;
}
