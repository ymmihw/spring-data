package com.ymmihw.spring.data.redis.pubsub.queue;

public interface MessagePublisher {
  void publish(final String message);
}
