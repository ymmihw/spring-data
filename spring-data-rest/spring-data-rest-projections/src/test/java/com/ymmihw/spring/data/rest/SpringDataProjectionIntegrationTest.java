package com.ymmihw.spring.data.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.ymmihw.spring.data.rest.models.Author;
import com.ymmihw.spring.data.rest.models.Book;
import com.ymmihw.spring.data.rest.repositories.AuthorRepository;
import com.ymmihw.spring.data.rest.repositories.BookRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(classes = SpringDataRestApplication.class,
    webEnvironment = WebEnvironment.DEFINED_PORT)

public class SpringDataProjectionIntegrationTest {
  private static final String BOOK_ENDPOINT = "http://localhost:8080/books";
  private static final String AUTHOR_ENDPOINT = "http://localhost:8080/authors";


  @Autowired
  private BookRepository bookRepo;

  @Autowired
  private AuthorRepository authorRepo;

  @BeforeEach
  public void setup() {
    if (!bookRepo.findById(1L).isPresent()) {
      Book book = new Book("Animal Farm");
      book.setIsbn("978-1943138425");
      book = bookRepo.save(book);
      Author author = new Author("George Orwell");
      author = authorRepo.save(author);
      author.setBooks(Arrays.asList(book));
      author = authorRepo.save(author);
    }
  }

  @Test
  public void whenGetBook_thenOK() {
    final Response response = RestAssured.get(BOOK_ENDPOINT + "/1");

    assertEquals(200, response.getStatusCode());
    assertTrue(response.asString().contains("isbn"));
    assertFalse(response.asString().contains("authorCount"));
  }


  @Test
  public void whenGetBookProjection_thenOK() {
    final Response response = RestAssured.get(BOOK_ENDPOINT + "/1?projection=customBook");

    assertEquals(200, response.getStatusCode());
    assertFalse(response.asString().contains("isbn"));
    assertTrue(response.asString().contains("authorCount"));
  }

  @Test
  public void whenGetAllBooks_thenOK() {
    final Response response = RestAssured.get(BOOK_ENDPOINT);

    assertEquals(200, response.getStatusCode());
    assertFalse(response.asString().contains("isbn"));
    assertTrue(response.asString().contains("authorCount"));
  }

  @Test
  public void whenGetAuthorBooks_thenOK() {
    final Response response = RestAssured.get(AUTHOR_ENDPOINT + "/1/books");

    assertEquals(200, response.getStatusCode());
    assertFalse(response.asString().contains("isbn"));
    assertTrue(response.asString().contains("authorCount"));
    System.out.println(response.asString());
  }
}
