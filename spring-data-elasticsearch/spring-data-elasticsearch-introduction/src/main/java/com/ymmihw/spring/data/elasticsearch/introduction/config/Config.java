package com.ymmihw.spring.data.elasticsearch.introduction.config;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableElasticsearchRepositories(
    basePackages = "com.ymmihw.spring.data.elasticsearch.introduction.repository")
@ComponentScan(basePackages = {"com.ymmihw.spring.data.elasticsearch.introduction.service"})
@RequiredArgsConstructor
public class Config {
  private final Client client;
  @Bean
  public ElasticsearchOperations elasticsearchTemplate() {
    return new ElasticsearchTemplate(client);
  }
}
