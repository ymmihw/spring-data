package com.ymmihw.spring.data.cassandra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource(value = {"classpath:cassandra.properties"})
@EnableCassandraRepositories(basePackages = "com.ymmihw.spring.data.cassandra.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {
  @Autowired
  private Environment environment;

  @Override
  protected String getKeyspaceName() {
    return environment.getProperty("cassandra.keyspace");
  }

  @Override
  protected String getContactPoints() {
    return environment.getProperty("cassandra.contactpoints");
  }

  @Override
  protected int getPort() {
    return Integer.parseInt(environment.getProperty("cassandra.port"));
  }

  // @Override
  // @Bean
  // @Override
  // public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
  // return new CassandraMappingContext();
  // }
}
