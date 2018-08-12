package com.ymmihw.spring.data.rest.repositories;

import org.springframework.data.repository.CrudRepository;
import com.ymmihw.spring.data.rest.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}
