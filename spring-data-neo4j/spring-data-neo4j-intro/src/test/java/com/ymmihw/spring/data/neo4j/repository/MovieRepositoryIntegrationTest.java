package com.ymmihw.spring.data.neo4j.repository;

import com.ymmihw.spring.data.neo4j.Application;
import com.ymmihw.spring.data.neo4j.domain.Movie;
import com.ymmihw.spring.data.neo4j.domain.MovieAndPersons;
import com.ymmihw.spring.data.neo4j.domain.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataNeo4jTest
@ContextConfiguration(classes = Application.class)
public class MovieRepositoryIntegrationTest {

  private static Neo4j embeddedDatabaseServer;

  @BeforeAll
  static void initializeNeo4j() {
    embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().withDisabledServer().build();
  }

  @AfterAll
  static void stopNeo4j() {
    embeddedDatabaseServer.close();
  }

  @DynamicPropertySource
  static void neo4jProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
    registry.add("spring.neo4j.authentication.username", () -> "neo4j");
    registry.add("spring.neo4j.authentication.password", () -> null);
  }

  @Autowired private MovieRepository movieRepository;

  @Autowired private PersonRepository personRepository;

  @BeforeEach
  public void initializeDatabase() {
    System.out.println("seeding embedded database");
    Movie italianJob = new Movie();
    italianJob.setTitle("The Italian Job");
    italianJob.setReleased(1999);
    movieRepository.save(italianJob);

    Person mark = new Person();
    mark.setName("Mark Wahlberg");

    List<Movie> movies = new ArrayList<>();
    movies.add(italianJob);
    //    mark.setRoles(roles);
    mark.setMovies(movies);
    personRepository.save(mark);
  }

  @Test
  @DirtiesContext
  public void testFindByTitle() {
    System.out.println("findByTitle");
    String title = "The Italian Job";
    Movie result = movieRepository.findByTitle(title);
    assertNotNull(result);
    assertEquals(1999, result.getReleased());
  }

  @Test
  @DirtiesContext
  public void testCount() {
    System.out.println("count");
    long movieCount = movieRepository.count();
    assertNotNull(movieCount);
    assertEquals(1, movieCount);
  }

  @Test
  @DirtiesContext
  public void testFindAll() {
    System.out.println("findAll");
    Collection<Movie> result = movieRepository.findAll();
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DirtiesContext
  public void testFindByTitleContaining() {
    System.out.println("findByTitleContaining");
    String title = "Italian";
    Collection<Movie> result = movieRepository.findByTitleContaining(title);
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DirtiesContext
  public void testGraph() {
    System.out.println("graph");
    List<MovieAndPersons> graph = movieRepository.graph(5);
    assertEquals(1, graph.size());
    MovieAndPersons movieAndPersons = graph.get(0);
    assertEquals("The Italian Job", movieAndPersons.getMovie().getTitle());
    assertEquals("Mark Wahlberg", movieAndPersons.getPerson().get(0).getName());
  }

  @Test
  @DirtiesContext
  public void testDeleteMovie() {
    System.out.println("deleteMovie");
    movieRepository.delete(movieRepository.findByTitle("The Italian Job"));
    assertNull(movieRepository.findByTitle("The Italian Job"));
  }

  @Test
  @DirtiesContext
  public void testDeleteAll() {
    System.out.println("deleteAll");
    movieRepository.deleteAll();
    Collection<Movie> result = movieRepository.findAll();
    assertEquals(0, result.size());
  }
}
