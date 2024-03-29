package com.ymmihw.spring.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.ymmihw.spring.data.mongodb.domain.Log;
import com.ymmihw.spring.data.mongodb.domain.LogLevel;
import com.ymmihw.spring.data.mongodb.service.ErrorLogsCounter;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Testcontainers
public class ErrorLogsCounterManualTest {

  @Container private static final MongoContainer CONTAINER = MongoContainer.getInstance();
  private static final String DB_NAME = "test";
  private static final String COLLECTION_NAME = Log.class.getName().toLowerCase();

  private static final int MAX_DOCUMENTS_IN_COLLECTION = 3;

  private ErrorLogsCounter errorLogsCounter;
  private MongoDatabase db;

  @BeforeEach
  public void setup() throws Exception {
    MongoTemplate mongoTemplate = initMongoTemplate();
    MongoCollection<Document> collection = createCappedCollection();

    persistDocument(collection, -1, LogLevel.ERROR, "my-service", "Initial log");

    errorLogsCounter = new ErrorLogsCounter(mongoTemplate, COLLECTION_NAME);
    Thread.sleep(1000L); // wait for initialization
  }

  private MongoTemplate initMongoTemplate() throws IOException {
    MongoClient mongoClient =
        MongoClients.create(
            "mongodb://"
                + CONTAINER.getContainerIpAddress()
                + ":"
                + CONTAINER.getFirstMappedPort());
    db = mongoClient.getDatabase(DB_NAME);

    return new MongoTemplate(mongoClient, DB_NAME);
  }

  private MongoCollection<Document> createCappedCollection() {
    db.createCollection(
        COLLECTION_NAME,
        new CreateCollectionOptions()
            .capped(true)
            .sizeInBytes(100000)
            .maxDocuments(MAX_DOCUMENTS_IN_COLLECTION));
    return db.getCollection(COLLECTION_NAME);
  }

  private void persistDocument(
      MongoCollection<Document> collection, int i, LogLevel level, String service, String message) {
    Document logMessage = new Document();
    logMessage.append("_id", i);
    logMessage.append("level", level.toString());
    logMessage.append("service", service);
    logMessage.append("message", message);
    collection.insertOne(logMessage);
  }

  @AfterEach
  public void tearDown() {
    CONTAINER.close();
  }

  @Test
  public void whenErrorLogsArePersisted_thenTheyAreReceivedByLogsCounter() throws Exception {
    MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);

    IntStream.range(1, 10)
        .forEach(
            i ->
                persistDocument(
                    collection,
                    i,
                    i > 5 ? LogLevel.ERROR : LogLevel.INFO,
                    "service" + i,
                    "Message from service " + i));

    Thread.sleep(1000L); // wait to receive all messages from the reactive mongodb driver

    assertThat(collection.countDocuments(), is((long) MAX_DOCUMENTS_IN_COLLECTION));
    assertThat(errorLogsCounter.count(), is(5));
  }
}
