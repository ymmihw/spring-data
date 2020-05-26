package com.ymmihw.spring.data.elasticsearch.tags.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ymmihw.spring.data.elasticsearch")
@ComponentScan(basePackages = {"com.ymmihw.spring.data.elasticsearch.tags.service"})
@RequiredArgsConstructor
public class Config {
  private final RestHighLevelClient client;

  @Bean
  public ElasticsearchOperations elasticsearchOperations() {
    return new ElasticsearchRestTemplate(client);
  }
}
