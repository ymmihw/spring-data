package com.ymmihw.spring.data.mongodb.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import com.ymmihw.spring.data.mongodb.BaseTest;
import com.ymmihw.spring.data.mongodb.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AccountMongoRepositoryIntegrationTest extends BaseTest {

  @Autowired
  AccountMongoRepository repository;

  @Before
  public void before() {
    repository.deleteAll().block();
  }

  @Test
  public void givenExample_whenFindAllWithExample_thenFindAllMacthings() {
    repository.save(new Account(null, "john", 12.3)).block();
    ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("owner", startsWith());
    Example<Account> example = Example.of(new Account(null, "jo", null), matcher);
    Flux<Account> accountFlux = repository.findAll(example);

    StepVerifier.create(accountFlux).assertNext(account -> assertEquals("john", account.getOwner()))
        .expectComplete().verify();
  }

  @Test
  public void givenAccount_whenSave_thenSave() {
    Mono<Account> accountMono = repository.save(new Account(null, "john", 12.3));

    StepVerifier.create(accountMono).assertNext(account -> assertNotNull(account.getId()))
        .expectComplete().verify();
  }

  @Test
  public void givenId_whenFindById_thenFindAccount() {
    Account inserted = repository.save(new Account(null, "john", 12.3)).block();
    Mono<Account> accountMono = repository.findById(inserted.getId());

    StepVerifier.create(accountMono).assertNext(account -> {
      assertEquals("john", account.getOwner());
      assertEquals(Double.valueOf(12.3), account.getValue());
      assertNotNull(account.getId());
    }).expectComplete().verify();
  }
}
