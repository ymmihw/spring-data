package com.ymmihw.spring.data.neo4j.repository;

import com.ymmihw.spring.data.neo4j.domain.Movie;
import com.ymmihw.spring.data.neo4j.domain.MovieAndPersons;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {

  Movie findByTitle(@Param("title") String title);

  @Query("MATCH (m:Movie) WHERE m.title =~ ('(?i).*'+$title+'.*') RETURN m")
  Collection<Movie> findByTitleContaining(@Param("title") String title);

  @Query(
      "MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) RETURN m as movie, collect(a) as person LIMIT $limit")
  List<MovieAndPersons> graph(@Param("limit") int limit);
}
