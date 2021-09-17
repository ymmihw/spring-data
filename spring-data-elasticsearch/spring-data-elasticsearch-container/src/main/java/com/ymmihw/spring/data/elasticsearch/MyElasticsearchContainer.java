package com.ymmihw.spring.data.elasticsearch;

import java.util.List;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class MyElasticsearchContainer extends ElasticsearchContainer {
  private static final DockerImageName myImage =
      DockerImageName.parse("elasticsearch:7.12.1")
          .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");
  private static MyElasticsearchContainer container;

  private MyElasticsearchContainer() {
    super(myImage);
  }

  public static MyElasticsearchContainer getInstance() {
    if (container == null) {
      container = new MyElasticsearchContainer();
    }
    return container;
  }

  @Override
  public void start() {
    List<String> env = getEnv();
    env.add("transport.host=0.0.0.0");
    env.add("cluster.name=elasticsearch");
    env.add("xpack.security.enabled=false");
    this.setEnv(env);
    super.start();
  }

  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
