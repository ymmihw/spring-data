package com.ymmihw.spring.data.rest.repositories;

import org.springframework.data.repository.CrudRepository;
import com.ymmihw.spring.data.rest.models.Library;

public interface LibraryRepository extends CrudRepository<Library, Long> {

}
