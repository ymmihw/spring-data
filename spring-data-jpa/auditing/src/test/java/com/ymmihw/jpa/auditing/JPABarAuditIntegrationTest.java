package com.ymmihw.jpa.auditing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import com.ymmihw.jpa.auditing.model.Bar;
import com.ymmihw.jpa.auditing.model.Bar.OPERATION;
import com.ymmihw.jpa.auditing.persist.jpa.service.IBarService;

@SpringBootTest(classes = {JpaApp.class})
public class JPABarAuditIntegrationTest {

  private static Logger logger = LoggerFactory.getLogger(JPABarAuditIntegrationTest.class);

  @Autowired
  @Qualifier("barJpaService")
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
  public final void whenBarsModified_thenBarsAudited() {

    // insert BAR1
    Bar bar1 = new Bar("BAR1");
    barService.create(bar1);

    // update BAR1
    bar1.setName("BAR1a");
    barService.update(bar1);

    // insert BAR2
    Bar bar2 = new Bar("BAR2");
    barService.create(bar2);

    // update BAR1
    bar1.setName("BAR1b");
    barService.update(bar1);

    // get BAR1 and BAR2 from the DB and check the audit values
    // detach instances from persistence context to make sure we fire db
    em.detach(bar1);
    em.detach(bar2);
    bar1 = barService.findOne(bar1.getId());
    bar2 = barService.findOne(bar2.getId());

    assertNotNull(bar1);
    assertNotNull(bar2);
    assertEquals(OPERATION.UPDATE, bar1.getOperation());
    assertEquals(OPERATION.INSERT, bar2.getOperation());
    assertTrue(bar1.getTimestamp() > bar2.getTimestamp());

    barService.deleteById(bar1.getId());
    barService.deleteById(bar2.getId());

  }

}
