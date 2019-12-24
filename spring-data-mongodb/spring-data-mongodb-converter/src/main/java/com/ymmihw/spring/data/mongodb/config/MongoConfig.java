package com.ymmihw.spring.data.mongodb.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClient;
import com.ymmihw.spring.data.mongodb.converter.ZonedDateTimeReadConverter;
import com.ymmihw.spring.data.mongodb.converter.ZonedDateTimeWriteConverter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMongoRepositories(basePackages = "com.ymmihw.spring.data.mongodb")
@RequiredArgsConstructor
public class MongoConfig extends AbstractMongoClientConfiguration {
  private final MongoClient mongoClient;

  @Override
  public MongoCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
    converters.add(new ZonedDateTimeReadConverter());
    converters.add(new ZonedDateTimeWriteConverter());
    return new MongoCustomConversions(converters);
  }

  @Override
  public MongoClient mongoClient() {
    return mongoClient;
  }

  @Override
  protected String getDatabaseName() {
    return "test";
  }
}
