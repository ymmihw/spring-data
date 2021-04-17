package com.ymmihw.spring.data.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import com.ymmihw.spring.data.rest.projections.CustomBook;


@Configuration
public class RestConfig implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(
      RepositoryRestConfiguration repositoryRestConfiguration) {
    repositoryRestConfiguration.getProjectionConfiguration().addProjection(CustomBook.class);
  }
}
