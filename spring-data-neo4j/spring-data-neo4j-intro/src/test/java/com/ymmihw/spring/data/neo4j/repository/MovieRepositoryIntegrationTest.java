package com.ymmihw.spring.data.neo4j.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import com.ymmihw.spring.data.neo4j.config.MovieDatabaseNeo4jTestConfiguration;
import com.ymmihw.spring.data.neo4j.domain.Movie;
import com.ymmihw.spring.data.neo4j.domain.Person;
import com.ymmihw.spring.data.neo4j.domain.Role;

@DataNeo4jTest
@ContextConfiguration(classes = MovieDatabaseNeo4jTestConfiguration.class)
public class MovieRepositoryIntegrationTest {

  private static ServerControls embeddedDatabaseServer;

  @BeforeAll
  static void initializeNeo4j() {

    embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder().newServer();
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

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private PersonRepository personRepository;


  @BeforeEach
  public void initializeDatabase() {
    System.out.println("seeding embedded database");
    Movie italianJob = new Movie();
    italianJob.setTitle("The Italian Job");
    italianJob.setReleased(1999);
    movieRepository.save(italianJob);

    Person mark = new Person();
    mark.setName("Mark Wahlberg");
    personRepository.save(mark);

    Role charlie = new Role();
    charlie.setMovie(italianJob);
    // charlie.setPerson(mark);
    Collection<String> roleNames = new HashSet<>();
    roleNames.add("Charlie Croker");
    charlie.setRoles(roleNames);
    List<Role> roles = new ArrayList<>();
    roles.add(charlie);
    italianJob.setRoles(roles);
    movieRepository.save(italianJob);

    List<Movie> movies = new ArrayList<>();
    movies.add(italianJob);
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
    List<Map<String, Object>> graph = movieRepository.graph(5);
    assertEquals(1, graph.size());
    Map<String, Object> map = graph.get(0);
    assertEquals(2, map.size());
    String[] cast = (String[]) map.get("cast");
    String movie = (String) map.get("movie");
    assertEquals("The Italian Job", movie);
    assertEquals("Mark Wahlberg", cast[0]);
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
