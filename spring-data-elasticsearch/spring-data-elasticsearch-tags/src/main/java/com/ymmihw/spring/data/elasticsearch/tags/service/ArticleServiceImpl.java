package com.ymmihw.spring.data.elasticsearch.tags.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ymmihw.spring.data.elasticsearch.tags.model.Article;
import com.ymmihw.spring.data.elasticsearch.tags.repository.ArticleRepository;

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
  public Iterable<Article> findAll() {
    return articleRepository.findAll();
  }


  @Override
  public long count() {
    return articleRepository.count();
  }

  @Override
  public void delete(Article article) {
    articleRepository.delete(article);
  }

  @Override
  public Page<Article> findByFilteredTagQuery(String tag, Pageable pageable) {
    return articleRepository.findByFilteredTagQuery(tag, pageable);
  }

  @Override
  public Page<Article> findByAuthorsNameAndFilteredTagQuery(String name, String tag,
      Pageable pageable) {
    return articleRepository.findByAuthorsNameAndFilteredTagQuery(name, tag, pageable);
  }

  @Override
  public Page<Article> findByTagUsingDeclaredQuery(String tag, Pageable pageable) {
    return articleRepository.findByTagUsingDeclaredQuery(tag, pageable);
  }
}
