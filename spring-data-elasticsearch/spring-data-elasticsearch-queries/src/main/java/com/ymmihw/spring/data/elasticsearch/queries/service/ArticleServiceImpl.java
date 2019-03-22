package com.ymmihw.spring.data.elasticsearch.queries.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ymmihw.spring.data.elasticsearch.queries.model.Article;
import com.ymmihw.spring.data.elasticsearch.queries.repository.ArticleRepository;

@Service
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  @Autowired
  public ArticleServiceImpl(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  @Override
  public Article save(Article article) {
    return articleRepository.save(article);
  }

  @Override
  public Optional<Article> findById(String id) {
    return articleRepository.findById(id);
  }

  @Override
  public Iterable<Article> findAll() {
    return articleRepository.findAll();
  }

  @Override
  public Page<Article> findByAuthorName(String name, Pageable pageable) {
    return articleRepository.findByAuthorsName(name, pageable);
  }

  @Override
  public Page<Article> findByAuthorNameUsingCustomQuery(String name, Pageable pageable) {
    return articleRepository.findByAuthorsNameUsingCustomQuery(name, pageable);
  }

  @Override
  public long count() {
    return articleRepository.count();
  }

  @Override
  public void delete(Article article) {
    articleRepository.delete(article);
  }
}
