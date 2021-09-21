package com.ymmihw.spring.data.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = SpringRedisReactiveApplication.class)
@TestPropertySource(locations = "classpath:/application.properties")
@Testcontainers
public class BaseTest {
  @Container public static RedisContainer container = RedisContainer.getInstance();

  @Test
  public void test() {}
}
