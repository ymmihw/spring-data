package com.ymmihw.spring.data.cassandra.model;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@PrimaryKeyClass
@AllArgsConstructor
@Getter
@Setter
public class BookKey implements Serializable {
  private static final long serialVersionUID = 1L;

  @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED,
      ordering = Ordering.DESCENDING)
  private UUID id;

  @PrimaryKeyColumn(name = "title", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
  private String title;

  @PrimaryKeyColumn(name = "publisher", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
  private String publisher;
}
