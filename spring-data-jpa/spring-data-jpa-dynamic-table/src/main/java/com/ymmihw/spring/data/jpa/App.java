package com.ymmihw.spring.data.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@RestController
public class App {
  private @Autowired FormEntryServiceImpl formEntryService;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @GetMapping(value = "/{formEntryId}/{formId}")
  public FormEntry getFormEntry(@PathVariable Long formId, @PathVariable Long formEntryId) {
    RequestContextHolder.setFormId(formEntryId);
    return formEntryService.findById(formId);
  }
}
