package com.ymmihw.spring.data.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.cassandra.model.Book;
import com.ymmihw.spring.data.cassandra.model.BookKey;

@Repository
public interface BookRepository extends CassandraRepository<Book, BookKey> {

  @Query("select * from book where title = ?0 and publisher=?1")
  Iterable<Book> findByTitleAndPublisher(String title, String publisher);
}
