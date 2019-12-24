package com.ymmihw.spring.data.redis;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedisTemplateListOpsIntegrationTest extends BaseTest {

  private static final String LIST_NAME = "demo_list";

  @Autowired
  private ReactiveRedisTemplate<String, String> reactiveRedisTemplateString;

  private ReactiveListOperations<String, String> reactiveListOps;

  @Before
  public void setup() {
    reactiveListOps = reactiveRedisTemplateString.opsForList();
  }

  @Test
  public void givenListAndValues_whenLeftPushAndLeftPop_thenLeftPushAndLeftPop() {
    Mono<Long> lPush = reactiveListOps.leftPushAll(LIST_NAME, "first", "second").log("Pushed");

    StepVerifier.create(lPush).expectNext(2L).verifyComplete();

    Mono<String> lPop = reactiveListOps.leftPop(LIST_NAME).log("Popped");

    StepVerifier.create(lPop).expectNext("second").verifyComplete();
  }

}
