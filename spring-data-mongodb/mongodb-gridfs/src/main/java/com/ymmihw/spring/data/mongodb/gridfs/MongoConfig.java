package com.ymmihw.spring.data.mongodb.gridfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
  @Autowired
  private MongoClient mongoClient;

  @Override
  protected String getDatabaseName() {
    return "test";
  }

  @Override
  public String getMappingBasePackage() {
    return "com.ymmihw.spring.data.mongodb.gridfs";
  }

  @Bean
  public GridFsTemplate gridFsTemplate() throws Exception {
    return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
  }

  @Override
  public MongoClient mongoClient() {
    return mongoClient;
  }
}
