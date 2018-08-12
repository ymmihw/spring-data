package com.ymmihw.spring.data.rest.repositories;

import org.springframework.data.repository.CrudRepository;
import com.ymmihw.spring.data.rest.models.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
