package com.ymmihw.spring.data.solr.repository;

import org.testcontainers.containers.GenericContainer;

public class SolrContainer extends GenericContainer<SolrContainer> {
  private static final String IMAGE_VERSION = "solr:7.7.3";
  private static SolrContainer container;

  private SolrContainer() {
    super(IMAGE_VERSION);
  }

  public static SolrContainer getInstance() {
    if (container == null) {
      container = new SolrContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.addExposedPort(8983);
    super.start();
    System.setProperty("SOLR_HOST", container.getContainerIpAddress());
    System.setProperty("SOLR_PORT", String.valueOf(container.getFirstMappedPort()));
    try {
      container.execInContainer("bin/solr", "create_core", "-c", "product");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
