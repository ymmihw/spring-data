package com.ymmihw.spring.data.mongodb.gridfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import com.mongodb.client.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
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
