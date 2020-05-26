package com.ymmihw.spring.data.cassandra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.ymmihw.spring.data.cassandra.repository")
public class SpringDataCassandraReactiveApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringDataCassandraReactiveApplication.class, args);
  }
}
