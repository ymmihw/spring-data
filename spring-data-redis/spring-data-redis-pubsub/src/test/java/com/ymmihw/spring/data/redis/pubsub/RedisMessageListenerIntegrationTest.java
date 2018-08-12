package com.ymmihw.spring.data.redis.pubsub;

import static org.junit.Assert.assertTrue;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.redis.pubsub.config.RedisConfig;
import com.ymmihw.spring.data.redis.pubsub.queue.RedisMessagePublisher;
import com.ymmihw.spring.data.redis.pubsub.queue.RedisMessageSubscriber;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisConfig.class)
public class RedisMessageListenerIntegrationTest {

  @Autowired
  private RedisMessagePublisher redisMessagePublisher;

  @Test
  public void testOnMessage() throws Exception {
    String message = "Message " + UUID.randomUUID();
    redisMessagePublisher.publish(message);
    Thread.sleep(100);
    assertTrue(RedisMessageSubscriber.messageList.get(0).contains(message));
  }
}
