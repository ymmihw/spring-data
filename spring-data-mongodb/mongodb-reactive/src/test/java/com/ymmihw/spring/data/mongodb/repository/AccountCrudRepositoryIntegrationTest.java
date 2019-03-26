package com.ymmihw.spring.data.mongodb.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.ymmihw.spring.data.mongodb.config.MongoReactiveConfig;
import com.ymmihw.spring.data.mongodb.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MongoReactiveConfig.class)
public class AccountCrudRepositoryIntegrationTest {

  @Autowired
  AccountCrudRepository repository;

  @Before
  public void before() {
    repository.deleteAll().block();
  }

  @Test
  public void givenValue_whenFindAllByValue_thenFindAccount() {
    double value = 12.34;
    repository.save(new Account(null, "Bill", value)).block();
    Flux<Account> accountFlux = repository.findAllByValue(value);

    StepVerifier.create(accountFlux).assertNext(account -> {
      System.out.println(account.getId());
      assertEquals("Bill", account.getOwner());
      assertEquals(Double.valueOf(value), account.getValue());
      assertNotNull(account.getId());
    }).expectComplete().verify();
  }

  @Test
  public void givenOwner_whenFindFirstByOwner_thenFindAccount() {
    double value = 12.5;
    repository.save(new Account(null, "Bill" + value, value)).block();
    Mono<Account> accountMono = repository.findFirstByOwner(Mono.just("Bill" + value));

    StepVerifier.create(accountMono).assertNext(account -> {
      assertEquals("Bill" + value, account.getOwner());
      assertEquals(Double.valueOf(value), account.getValue());
      assertNotNull(account.getId());
    }).expectComplete().verify();

  }

  @Test
  public void givenAccount_whenSave_thenSaveAccount() {
    Mono<Account> accountMono = repository.save(new Account(null, "Bill", 12.3));

    StepVerifier.create(accountMono).assertNext(account -> assertNotNull(account.getId()))
        .expectComplete().verify();
  }


}
