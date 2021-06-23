package com.ymmihw.spring.data.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import com.ymmihw.spring.data.rest.projections.CustomBook;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
  @Override
  public void configureRepositoryRestConfiguration(
      RepositoryRestConfiguration repositoryRestConfiguration, CorsRegistry cors) {
    repositoryRestConfiguration.getProjectionConfiguration().addProjection(CustomBook.class);
  }
}
