package com.ymmihw.spring.data.rest.projections;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import com.ymmihw.spring.data.rest.models.Author;
import com.ymmihw.spring.data.rest.models.Book;

@Projection(name = "customBook", types = {Book.class})
public interface CustomBook {
  @Value("#{target.id}")
  long getId();

  String getTitle();

  List<Author> getAuthors();

  @Value("#{target.getAuthors().size()}")
  int getAuthorCount();
}
