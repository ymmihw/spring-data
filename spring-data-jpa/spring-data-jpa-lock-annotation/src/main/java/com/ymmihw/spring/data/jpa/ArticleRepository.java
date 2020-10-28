package com.ymmihw.spring.data.jpa;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends CrudRepository<Article, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select a from Article a where a.id = :id")
  Article findArticleForWrite(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_READ)
  @Query("select a from Article a where a.id = :id")
  Article findArticleForRead(@Param("id") Long id);

  @Lock(LockModeType.PESSIMISTIC_READ)
  List<Article> findAllByContent(String content);
}
