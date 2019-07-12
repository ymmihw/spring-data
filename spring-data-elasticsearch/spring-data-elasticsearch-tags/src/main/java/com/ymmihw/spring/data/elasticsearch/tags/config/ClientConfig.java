package com.ymmihw.spring.data.elasticsearch.tags.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
  @Bean
  public Client client() {
    Settings settings = Settings.builder().build();
    PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
    try {
      TransportClient addTransportAddress = preBuiltTransportClient
          .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
      return addTransportAddress;
    } catch (final UnknownHostException ioex) {
      throw new RuntimeException(ioex);
    }
  }
}
