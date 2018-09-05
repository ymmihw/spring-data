package com.ymmihw.spring.data.rest;

import static org.mockito.Mockito.mock;
import org.junit.Test;
import org.mockito.Mockito;
import com.ymmihw.spring.data.rest.evets.BookEventHandler;
import com.ymmihw.spring.data.rest.models.Author;
import com.ymmihw.spring.data.rest.models.Book;

public class BookEventHandlerUnitTest {
  @Test
  public void whenCreateBookThenSuccess() {
    Book book = mock(Book.class);
    BookEventHandler bookEventHandler = new BookEventHandler();
    bookEventHandler.handleBookBeforeCreate(book);
    Mockito.verify(book, Mockito.times(1)).getAuthors();

  }

  @Test
  public void whenCreateAuthorThenSuccess() {
    Author author = mock(Author.class);
    BookEventHandler bookEventHandler = new BookEventHandler();
    bookEventHandler.handleAuthorBeforeCreate(author);
    Mockito.verify(author, Mockito.times(1)).getBooks();

  }
}
