package com.ymmihw.spring.data.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ymmihw.spring.data.rest.validators.WebsiteUserValidator;

@SpringBootApplication
public class SpringDataRestApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringDataRestApplication.class, args);
  }

  @Bean
  public WebsiteUserValidator beforeCreateWebsiteUserValidator() {
    return new WebsiteUserValidator();
  }
}
