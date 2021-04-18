package com.ymmihw.spring.data.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.ymmihw.spring.data.rest.models.Address;
import com.ymmihw.spring.data.rest.models.Author;
import com.ymmihw.spring.data.rest.models.Book;
import com.ymmihw.spring.data.rest.models.Library;

@SpringBootTest(classes = SpringDataRestApplication.class,
    webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringDataRelationshipsIntegrationTest {

  @Autowired
  private TestRestTemplate template;

  private static final String BOOK_ENDPOINT = "http://localhost:8080/books/";
  private static final String AUTHOR_ENDPOINT = "http://localhost:8080/authors/";
  private static final String ADDRESS_ENDPOINT = "http://localhost:8080/addresses/";
  private static final String LIBRARY_ENDPOINT = "http://localhost:8080/libraries/";

  private static final String LIBRARY_NAME = "My Library";
  private static final String AUTHOR_NAME = "George Orwell";

  @Test
  public void whenSaveOneToOneRelationship_thenCorrect() throws JSONException {
    Library library = new Library(LIBRARY_NAME);
    template.postForEntity(LIBRARY_ENDPOINT, library, Library.class);

    Address address = new Address("Main street, nr 1");
    template.postForEntity(ADDRESS_ENDPOINT, address, Address.class);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("Content-type", "text/uri-list");
    HttpEntity<String> httpEntity = new HttpEntity<>(ADDRESS_ENDPOINT + "/1", requestHeaders);
    template.exchange(LIBRARY_ENDPOINT + "/1/libraryAddress", HttpMethod.PUT, httpEntity,
        String.class);

    ResponseEntity<Library> libraryGetResponse =
        template.getForEntity(ADDRESS_ENDPOINT + "/1/library", Library.class);
    assertEquals(libraryGetResponse.getBody().getName(), LIBRARY_NAME, "library is incorrect");
  }

  @Test
  public void whenSaveOneToManyRelationship_thenCorrect() throws JSONException {
    Library library = new Library(LIBRARY_NAME);
    template.postForEntity(LIBRARY_ENDPOINT, library, Library.class);

    Book book1 = new Book("Dune");
    template.postForEntity(BOOK_ENDPOINT, book1, Book.class);

    Book book2 = new Book("1984");
    template.postForEntity(BOOK_ENDPOINT, book2, Book.class);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("Content-type", "text/uri-list");
    HttpEntity<String> bookHttpEntity = new HttpEntity<>(LIBRARY_ENDPOINT + "/1", requestHeaders);
    template.exchange(BOOK_ENDPOINT + "/1/library", HttpMethod.PUT, bookHttpEntity, String.class);
    template.exchange(BOOK_ENDPOINT + "/2/library", HttpMethod.PUT, bookHttpEntity, String.class);

    ResponseEntity<Library> libraryGetResponse =
        template.getForEntity(BOOK_ENDPOINT + "/1/library", Library.class);
    assertEquals(libraryGetResponse.getBody().getName(), LIBRARY_NAME, "library is incorrect");
  }

  @Test
  public void whenSaveManyToManyRelationship_thenCorrect() throws JSONException {
    Author author1 = new Author(AUTHOR_NAME);
    template.postForEntity(AUTHOR_ENDPOINT, author1, Author.class);

    Book book1 = new Book("Animal Farm");
    template.postForEntity(BOOK_ENDPOINT, book1, Book.class);

    Book book2 = new Book("1984");
    template.postForEntity(BOOK_ENDPOINT, book2, Book.class);

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("Content-type", "text/uri-list");
    HttpEntity<String> httpEntity =
        new HttpEntity<>(BOOK_ENDPOINT + "/1\n" + BOOK_ENDPOINT + "/2", requestHeaders);
    template.exchange(AUTHOR_ENDPOINT + "/1/books", HttpMethod.PUT, httpEntity, String.class);

    String jsonResponse = template.getForObject(BOOK_ENDPOINT + "/1/authors", String.class);
    JSONObject jsonObj = new JSONObject(jsonResponse).getJSONObject("_embedded");
    JSONArray jsonArray = jsonObj.getJSONArray("authors");
    assertEquals(jsonArray.getJSONObject(0).getString("name"), AUTHOR_NAME, "author is incorrect");
  }
}
