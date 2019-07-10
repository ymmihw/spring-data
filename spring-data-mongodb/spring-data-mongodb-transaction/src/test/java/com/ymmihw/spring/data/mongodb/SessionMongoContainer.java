package com.ymmihw.spring.data.mongodb;

public class SessionMongoContainer extends MongoContainer {
  @Override
  public void start() {
    this.setCommand("--replSet", "rs0");
    super.start();
    try {
      execInContainer("mongo", "--eval", "rs.initiate()\r\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
