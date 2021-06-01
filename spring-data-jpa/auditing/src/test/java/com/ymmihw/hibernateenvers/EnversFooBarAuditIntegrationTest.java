package com.ymmihw.hibernateenvers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.hibernate.service.IBarAuditableService;
import com.ymmihw.hibernateenvers.persist.hibernate.service.IFooAuditableService;


@SpringBootTest(classes = HibernateEnversApp.class)
public class EnversFooBarAuditIntegrationTest {

  private static Logger logger = LoggerFactory.getLogger(EnversFooBarAuditIntegrationTest.class);

  @BeforeAll
  public static void setUpBeforeClass() throws Exception {
    logger.info("setUpBeforeClass()");
  }

  @AfterAll
  public static void tearDownAfterClass() throws Exception {
    logger.info("tearDownAfterClass()");
  }

  @Autowired
  @Qualifier("fooAuditableService")
  private IFooAuditableService fooService;

  @Autowired
  @Qualifier("barAuditableService")
  private IBarAuditableService barService;

  @Autowired
  private SessionFactory sessionFactory;

  private Session session;

  @BeforeEach
  public void setUp() throws Exception {
    logger.info("setUp()");
    makeRevisions();
    session = sessionFactory.openSession();
  }

  @AfterEach
  public void tearDown() throws Exception {
    logger.info("tearDown()");
    session.close();
  }

  private void makeRevisions() {
    final Bar bar = rev1();
    rev2(bar);
    rev3(bar);
    rev4(bar);
  }

  // REV #1: insert BAR & FOO1
  private Bar rev1() {
    final Bar bar = new Bar("BAR");
    final Foo foo1 = new Foo("FOO1");
    foo1.setBar(bar);
    fooService.create(foo1);
    return bar;
  }

  // REV #2: insert FOO2 & update BAR
  private void rev2(final Bar bar) {
    final Foo foo2 = new Foo("FOO2");
    foo2.setBar(bar);
    fooService.create(foo2);
  }

  // REV #3: update BAR
  private void rev3(final Bar bar) {

    bar.setName("BAR1");
    barService.update(bar);
  }

  // REV #4: insert FOO3 & update BAR
  private void rev4(final Bar bar) {

    final Foo foo3 = new Foo("FOO3");
    foo3.setBar(bar);
    fooService.create(foo3);
  }

  @Test
  public final void whenFooBarsModified_thenFooBarsAudited() {

    List<Bar> barRevisionList;
    List<Foo> fooRevisionList;

    // test Bar revisions

    barRevisionList = barService.getRevisions();

    assertNotNull(barRevisionList);
    assertEquals(4, barRevisionList.size());

    assertEquals("BAR", barRevisionList.get(0).getName());
    assertEquals("BAR", barRevisionList.get(1).getName());
    assertEquals("BAR1", barRevisionList.get(2).getName());
    assertEquals("BAR1", barRevisionList.get(3).getName());

    assertEquals(1, barRevisionList.get(0).getFooSet().size());
    assertEquals(2, barRevisionList.get(1).getFooSet().size());
    assertEquals(2, barRevisionList.get(2).getFooSet().size());
    assertEquals(3, barRevisionList.get(3).getFooSet().size());

    // test Foo revisions

    fooRevisionList = fooService.getRevisions();
    assertNotNull(fooRevisionList);
    assertEquals(3, fooRevisionList.size());
    assertEquals("FOO1", fooRevisionList.get(0).getName());
    assertEquals("FOO2", fooRevisionList.get(1).getName());
    assertEquals("FOO3", fooRevisionList.get(2).getName());
  }

}
