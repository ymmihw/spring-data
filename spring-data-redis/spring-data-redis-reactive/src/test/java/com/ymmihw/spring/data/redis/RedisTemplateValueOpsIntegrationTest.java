package com.ymmihw.spring.data.redis;


import java.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import com.ymmihw.spring.data.redis.model.Employee;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class RedisTemplateValueOpsIntegrationTest extends BaseTest {

  @Autowired
  private ReactiveRedisTemplate<String, Employee> redisTemplate;

  private ReactiveValueOperations<String, Employee> reactiveValueOps;

  @Before
  public void setup() {
    reactiveValueOps = redisTemplate.opsForValue();
  }

  @Test
  public void givenEmployee_whenSet_thenSet() {

    Mono<Boolean> result = reactiveValueOps.set("123", new Employee("123", "Bill", "Accounts"));

    StepVerifier.create(result).expectNext(true).verifyComplete();
  }

  @Test
  public void givenEmployeeId_whenGet_thenReturnsEmployee() {

    Mono<Employee> fetchedEmployee = reactiveValueOps.get("123");

    StepVerifier.create(fetchedEmployee).expectNext(new Employee("123", "Bill", "Accounts"))
        .verifyComplete();
  }

  @Test
  public void givenEmployee_whenSetWithExpiry_thenSetsWithExpiryTime() throws InterruptedException {

    Mono<Boolean> result = reactiveValueOps.set("129", new Employee("129", "John", "Programming"),
        Duration.ofSeconds(1));

    Mono<Employee> fetchedEmployee = reactiveValueOps.get("129");

    StepVerifier.create(result).expectNext(true).verifyComplete();

    Thread.sleep(2000L);

    StepVerifier.create(fetchedEmployee).expectNextCount(0L).verifyComplete();
  }

}
