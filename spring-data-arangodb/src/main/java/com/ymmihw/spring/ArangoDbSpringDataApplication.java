package com.ymmihw.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class ArangoDbSpringDataApplication {

  public static void main(String[] args) {
    SpringApplication.run(ArangoDbSpringDataApplication.class, args);
  }
}
