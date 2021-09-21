package com.ymmihw.spring.data.redis.pubsub;

import com.ymmihw.spring.data.redis.RedisContainer;
import com.ymmihw.spring.data.redis.pubsub.config.RedisConfig;
import com.ymmihw.spring.data.redis.pubsub.queue.RedisMessagePublisher;
import com.ymmihw.spring.data.redis.pubsub.queue.RedisMessageSubscriber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ContextConfiguration(
    classes = {RedisConfig.class},
    loader = AnnotationConfigContextLoader.class)
@TestPropertySource(locations = "classpath:/application.properties")
@Testcontainers
public class RedisMessageListenerIntegrationTest {
  @Container public static RedisContainer container = RedisContainer.getInstance();
  @Autowired private RedisMessagePublisher redisMessagePublisher;

  @Test
  public void testOnMessage() throws Exception {
    String message = "Message " + UUID.randomUUID();
    redisMessagePublisher.publish(message);
    Thread.sleep(100);
    assertTrue(RedisMessageSubscriber.messageList.get(0).contains(message));
  }
}
