package com.ymmihw.spring.data.redis;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(locations = "classpath:/application.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = SpringRedisReactiveApplication.class)
public class BaseTest {
  @ClassRule
  public static RedisContainer container = RedisContainer.getInstance();

  @Test
  public void test() {

  }
}
