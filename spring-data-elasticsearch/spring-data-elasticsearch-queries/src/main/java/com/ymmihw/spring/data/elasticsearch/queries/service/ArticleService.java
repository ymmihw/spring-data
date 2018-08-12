package com.ymmihw.spring.data.elasticsearch.queries.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ymmihw.spring.data.elasticsearch.queries.model.Article;

public interface ArticleService {
    Article save(Article article);

    Iterable<Article> findAll();

    Page<Article> findByAuthorName(String name, Pageable pageable);

    Page<Article> findByAuthorNameUsingCustomQuery(String name, Pageable pageable);

    long count();

    void delete(Article article);

    Optional<Article> findById(String id);
}