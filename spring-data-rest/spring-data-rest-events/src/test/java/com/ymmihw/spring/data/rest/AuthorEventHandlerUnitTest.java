package com.ymmihw.spring.data.rest;

import static org.mockito.Mockito.mock;
import org.junit.Test;
import org.mockito.Mockito;
import com.ymmihw.spring.data.rest.evets.AuthorEventHandler;
import com.ymmihw.spring.data.rest.models.Author;

public class AuthorEventHandlerUnitTest {

  @Test
  public void whenCreateAuthorThenSuccess() {
    Author author = mock(Author.class);
    AuthorEventHandler authorEventHandler = new AuthorEventHandler();
    authorEventHandler.handleAuthorBeforeCreate(author);
    Mockito.verify(author, Mockito.times(1)).getName();

  }

  @Test
  public void whenDeleteAuthorThenSuccess() {
    Author author = mock(Author.class);
    AuthorEventHandler authorEventHandler = new AuthorEventHandler();
    authorEventHandler.handleAuthorAfterDelete(author);
    Mockito.verify(author, Mockito.times(1)).getName();

  }
}
