package com.ymmihw.spring.data.mongodb.gridfs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GridFsTemplateConfig {
  private final MongoConverter mongoConverter;
  private final MongoDatabaseFactory mongoDatabaseFactory;

  @Bean
  public GridFsTemplate gridFsTemplate() throws Exception {
    return new GridFsTemplate(mongoDatabaseFactory, mongoConverter);
  }
}
