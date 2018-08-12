package com.ymmihw.spring.data.elasticsearch.tags.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ymmihw.spring.data.elasticsearch.tags.model.Article;

public interface ArticleService {
  Article save(Article article);

  Iterable<Article> findAll();

  Page<Article> findByFilteredTagQuery(String tag, Pageable pageable);

  Page<Article> findByAuthorsNameAndFilteredTagQuery(String name, String tag, Pageable pageable);

  Page<Article> findByTagUsingDeclaredQuery(String tag, Pageable pageable);

  long count();

  void delete(Article article);
}
