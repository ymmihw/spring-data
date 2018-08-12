package com.ymmihw.spring.data.mongodb.custom.cascading.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.ymmihw.spring.data.mongodb.custom.cascading.annotation.CascadeSave;

@Document
public class User {

  @Id
  private String id;
  private String name;
  @DBRef
  @CascadeSave
  private EmailAddress emailAddress;

  public User() {}

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
  }
}
