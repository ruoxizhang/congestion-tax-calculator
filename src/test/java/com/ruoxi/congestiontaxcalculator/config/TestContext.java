package com.ruoxi.congestiontaxcalculator.config;

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.testcontainers.containers.MongoDBContainer;

public class TestContext implements AutoCloseable, CloseableResource {

  private final MongoDBContainer mongoDBContainer;

  public TestContext() {
    mongoDBContainer = mongoDB();
  }

  private static MongoDBContainer mongoDB() {
    var container = new MongoDBContainer("mongo:4.0.23");
    container.start();
    var mongoDbUri = container.getReplicaSetUrl();
    System.setProperty("spring.data.mongodb.uri", mongoDbUri);
    return container;
  }

  public static TestContext build(String key) {
    return new TestContext();
  }

  @Override
  public void close() {
    mongoDBContainer.close();
  }
}
