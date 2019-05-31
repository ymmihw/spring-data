package com.ymmihw.spring.data.redis;

import org.testcontainers.containers.GenericContainer;

public class RedisContainer extends GenericContainer<RedisContainer> {
  private static final String IMAGE_VERSION = "redis:5.0.5-alpine";
  private static RedisContainer container;

  private RedisContainer() {
    super(IMAGE_VERSION);
  }

  public static RedisContainer getInstance() {
    if (container == null) {
      container = new RedisContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.addExposedPort(6379);
    super.start();
    System.setProperty("REDIS_HOST", container.getContainerIpAddress());
    System.setProperty("REDIS_PORT", String.valueOf(container.getFirstMappedPort()));
  }


  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
