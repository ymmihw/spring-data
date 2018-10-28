package com.ymmihw.spring.data.neo4j.config;

import org.neo4j.ogm.config.Configuration.Builder;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@ComponentScan(basePackages = {"com.ymmihw.spring.data.neo4j.services"})
@Configuration
@EnableNeo4jRepositories(basePackages = "com.ymmihw.spring.data.neo4j.repository")
public class MovieDatabaseNeo4jConfiguration {

  public static final String URL = System.getenv("NEO4J_URL") != null ? System.getenv("NEO4J_URL")
      : "http://neo4j:movies@localhost:7474";

  @Bean
  public org.neo4j.ogm.config.Configuration configuration() {
    org.neo4j.ogm.config.Configuration config = new Builder().uri(URL).build();
    return config;
  }

  @Bean
  public SessionFactory sessionFactory() {
    return new SessionFactory(configuration(), "com.ymmihw.spring.data.neo4j.domain");
  }
}
