package com.ymmihw.spring.data.jpa;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ExampleClient {
  @Autowired
  private ArticleRepository repo;
  @Autowired
  private Tasks tasks;

  public ExecutorService run() {
    // creating and persisting an Article
    Article article = new Article("test article");
    repo.save(article);

    ExecutorService es = Executors.newFixedThreadPool(2);

    // user 1, reader
    es.execute(tasks::runUser1Transaction);

    // user 2, writer
    es.execute(tasks::runUser2Transaction);

    return es;
  }

  @Service
  @Transactional
  public class Tasks {
    public void runUser1Transaction() {
      System.out.println(" -- user 1 reading Article entity --");
      long start = System.currentTimeMillis();
      List<Article> article1 = null;
      try {
        article1 = repo.findAllByContent("test article");
      } catch (Exception e) {
        System.err.println("User 1 got exception while acquiring the database lock:\n " + e);
        return;
      }
      System.out
          .println("user 1 got the lock, block time was: " + (System.currentTimeMillis() - start));
      // delay for 2 secs
      ThreadSleep(3000);
      System.out.println("User 1 read article: " + article1);
    }

    public void runUser2Transaction() {
      ThreadSleep(500);// let user1 acquire optimistic lock first
      System.out.println(" -- user 2 writing Article entity --");
      long start = System.currentTimeMillis();
      Article article2 = null;
      try {
        article2 = repo.findArticleForWrite(1L);
      } catch (Exception e) {
        System.err.println("User 2 got exception while acquiring the database lock:\n " + e);
        return;
      }
      System.out
          .println("user 2 got the lock, block time was: " + (System.currentTimeMillis() - start));
      article2.setContent("updated content by user 2.");
      repo.save(article2);
      System.out.println("User 2 updated article: " + article2);
    }

    private void ThreadSleep(long timeout) {
      try {
        Thread.sleep(timeout);
      } catch (InterruptedException e) {
        System.err.println(e);
      }
    }
  }


  public static void main(String[] args) throws InterruptedException {
    ConfigurableApplicationContext context = SpringApplication.run(ExampleClient.class, args);
    ExampleClient exampleClient = context.getBean(ExampleClient.class);
    ExecutorService es = exampleClient.run();
    es.shutdown();
    es.awaitTermination(5, TimeUnit.MINUTES);
    EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
    emf.close();
  }
}
