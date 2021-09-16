package com.ymmihw.spring;

import java.util.List;
import org.testcontainers.containers.GenericContainer;

public class ArangoContainer extends GenericContainer<ArangoContainer> {
  private static final String IMAGE_VERSION = "arangodb:3.8.1";
  private static ArangoContainer container;

  public ArangoContainer() {
    super(IMAGE_VERSION);
  }

  public static ArangoContainer getInstance() {
    if (container == null) {
      container = new ArangoContainer();
    }
    return container;
  }

  @Override
  public void start() {
    List<String> env = getEnv();
    env.add("ARANGO_ROOT_PASSWORD=password");
    this.setEnv(env);
    super.addExposedPort(8529);
    super.start();
  }

  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
