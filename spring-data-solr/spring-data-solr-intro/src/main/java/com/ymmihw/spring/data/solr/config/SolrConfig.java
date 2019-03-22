package com.ymmihw.spring.data.solr.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = "com.ymmihw.spring.data.solr.repository",
    namedQueriesLocation = "classpath:solr-named-queries.properties")
public class SolrConfig {
  @Bean
  public SolrClient solrClient() {
    return new HttpSolrClient.Builder().withBaseSolrUrl("http://192.168.10.177:8983/solr/")
        .build();
  }

  @Bean
  public SolrTemplate solrTemplate(SolrClient client) throws Exception {
    return new SolrTemplate(client);
  }
}
