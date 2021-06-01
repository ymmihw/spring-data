package com.ymmihw.springdatajpa.auditing;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import com.ymmihw.springdatajpa.auditing.model.Bar;
import com.ymmihw.springdatajpa.auditing.persist.springdatajpa.service.IBarService;

@SpringBootTest(classes = {SpringDataJpaApp.class})
public class SpringDataJPABarAuditIntegrationTest {

  private static Logger logger =
      LoggerFactory.getLogger(SpringDataJPABarAuditIntegrationTest.class);

  @BeforeAll
  public static void setUpBeforeClass() throws Exception {
    logger.info("setUpBeforeClass()");
  }

  @AfterAll
  public static void tearDownAfterClass() throws Exception {
    logger.info("tearDownAfterClass()");
  }

  @Autowired
  @Qualifier("barSpringDataJpaService")
  private IBarService barService;

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  private EntityManager em;

  @BeforeEach
  public void setUp() throws Exception {
    logger.info("setUp()");
    em = entityManagerFactory.createEntityManager();
  }

  @AfterEach
  public void tearDown() throws Exception {
    logger.info("tearDown()");
    em.close();
  }

  @Test
  @WithMockUser(username = "tutorialuser")
  public final void whenBarsModified_thenBarsAudited() {
    Bar bar = new Bar("BAR1");
    barService.create(bar);
    assertEquals(bar.getCreatedDate(), bar.getModifiedDate());
    assertEquals("tutorialuser", bar.getCreatedBy(), bar.getModifiedBy());
    bar.setName("BAR2");
    bar = barService.update(bar);
    assertTrue(bar.getCreatedDate() < bar.getModifiedDate());
    assertEquals("tutorialuser", bar.getCreatedBy(), bar.getModifiedBy());
  }
}
