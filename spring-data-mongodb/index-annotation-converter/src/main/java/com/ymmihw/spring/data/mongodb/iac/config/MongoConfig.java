package com.ymmihw.spring.data.mongodb.iac.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.ymmihw.spring.data.mongodb.iac.converter.UserWriterConverter;
import com.ymmihw.spring.data.mongodb.iac.event.CascadeSaveMongoEventListener;

@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb.iac.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Autowired
  private MongoClient mongoClient;

  @Override
  public MongoCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
    converters.add(new UserWriterConverter());
    return new MongoCustomConversions(converters);
  }

  @Override
  protected String getDatabaseName() {
    return "test";
  }

  @Bean
  public CascadeSaveMongoEventListener cascadingMongoEventListener() {
    return new CascadeSaveMongoEventListener();
  }

  @Override
  public MongoClient mongoClient() {
    return mongoClient;
  }
}
