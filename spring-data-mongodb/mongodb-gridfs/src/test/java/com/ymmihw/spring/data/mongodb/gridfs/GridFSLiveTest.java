package com.ymmihw.spring.data.mongodb.gridfs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.bson.Document;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.ymmihw.spring.data.mongodb.MongoContainer;
import com.ymmihw.spring.data.mongodb.gridfs.GridFSLiveTest.MongoClientDockerConfig;

@ContextConfiguration(
    classes = {MongoConfig.class, MongoClientDockerConfig.class, GridFsTemplateConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GridFSLiveTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @ClassRule
  public static MongoContainer container = MongoContainer.getInstance();

  @Configuration
  public static class MongoClientDockerConfig {
    @Bean
    public MongoClient mongo() throws Exception {
      MongoClient client = MongoClients.create(
          "mongodb://" + container.getContainerIpAddress() + ":" + container.getFirstMappedPort());
      return client;
    }
  }

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @After
  public void tearDown() {
    GridFSFindIterable fileList = gridFsTemplate.find(new Query());
    for (GridFSFile file : fileList) {
      gridFsTemplate.delete(new Query(Criteria.where("filename").is(file.getFilename())));
    }
  }

  @Test
  public void whenStoringFileWithMetadata_thenFileAndMetadataAreStored() throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("user", "alex");
    String id = "";
    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    assertNotNull(id);
  }

  @Test
  public void givenFileWithMetadataExist_whenFindingFileById_thenFileWithMetadataIsFound()
      throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("user", "alex");
    String id = "";
    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    GridFSFile gridFSDBFile = gridFsTemplate.findOne(new Query());

    assertNotNull(gridFSDBFile);
    Document document = gridFSDBFile.getMetadata();
    assertNotNull(document);
    assertThat(gridFSDBFile.getObjectId().toString(), is(id));
    assertThat(document.keySet().size(), is(2));
    assertNotNull(gridFSDBFile.getUploadDate());
    assertNotNull(gridFSDBFile.getChunkSize());
    assertThat(document.get("_contentType"), is("image/png"));
    assertThat(gridFSDBFile.getFilename(), is("test.png"));
    assertThat(document.get("user"), is("alex"));
  }

  @Test
  public void givenMetadataAndFilesExist_whenFindingAllFiles_thenFilesWithMetadataAreFound()
      throws IOException {
    DBObject metaDataUser1 = new BasicDBObject();
    metaDataUser1.put("user", "alex");
    DBObject metaDataUser2 = new BasicDBObject();
    metaDataUser2.put("user", "david");

    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser1);
      gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser2);
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    GridFSFindIterable iterable = gridFsTemplate.find(new Query());

    assertThat(count(iterable), is(2));
  }

  private static int count(GridFSFindIterable iterable) {
    int i = 0;
    for (MongoCursor<GridFSFile> it = iterable.iterator(); it.hasNext(); it.next()) {
      i++;
    }
    return i;
  }

  @Test
  public void givenMetadataAndFilesExist_whenFindingAllFilesOnQuery_thenFilesWithMetadataAreFoundOnQuery()
      throws IOException {
    DBObject metaDataUser1 = new BasicDBObject();
    metaDataUser1.put("user", "alex");
    DBObject metaDataUser2 = new BasicDBObject();
    metaDataUser2.put("user", "david");

    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser1);
      gridFsTemplate.store(inputStream, "test.png", "image/png", metaDataUser2);
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    GridFSFindIterable iterable =
        gridFsTemplate.find(new Query(Criteria.where("metadata.user").is("alex")));

    assertNotNull(iterable);
    assertThat(count(iterable), is(1));
  }

  @Test
  public void givenFileWithMetadataExist_whenDeletingFileById_thenFileWithMetadataIsDeleted()
      throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("user", "alex");
    String id = "";
    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      id = gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));

    assertThat(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id))), is(nullValue()));
  }

  @Test
  public void givenFileWithMetadataExist_whenGettingFileByResource_thenFileWithMetadataIsGotten()
      throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("user", "alex");
    try (InputStream inputStream = new FileInputStream("src/main/resources/test.png")) {
      gridFsTemplate.store(inputStream, "test.png", "image/png", metaData).toString();
    } catch (FileNotFoundException ex) {
      logger.error("File not found", ex);
    }

    GridFsResource[] gridFsResource = gridFsTemplate.getResources("test*");

    assertNotNull(gridFsResource);
    assertEquals(gridFsResource.length, 1);
    assertThat(gridFsResource[0].getFilename(), is("test.png"));
  }
}
