package com.ymmihw.spring.data.mongodb.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.ymmihw.spring.data.mongodb.Spring5ReactiveApplication;
import com.ymmihw.spring.data.mongodb.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Spring5ReactiveApplication.class)
public class AccountTemplateOperationsIntegrationTest {

  @Autowired
  AccountTemplateOperations accountTemplate;

  @Before
  public void before() {
    accountTemplate.deleteAll();
  }

  @Test
  public void givenAccount_whenSave_thenSave() {
    Account account = accountTemplate.save(Mono.just(new Account(null, "Raul", 12.3))).block();
    assertNotNull(account.getId());
  }

  @Test
  public void givenId_whenFindById_thenFindAccount() {
    Mono<Account> accountMono = accountTemplate.save(Mono.just(new Account(null, "Raul", 12.3)));
    Mono<Account> accountMonoResult = accountTemplate.findById(accountMono.block().getId());
    assertNotNull(accountMonoResult.block().getId());
    assertEquals(accountMonoResult.block().getOwner(), "Raul");
  }

  @Test
  public void whenFindAll_thenFindAllAccounts() {
    Account account1 = accountTemplate.save(Mono.just(new Account(null, "Raul", 12.3))).block();
    Account account2 =
        accountTemplate.save(Mono.just(new Account(null, "Raul Torres", 13.3))).block();
    Flux<Account> accountFlux = accountTemplate.findAll();
    List<Account> accounts = accountFlux.collectList().block();
    assertTrue(accounts.stream().anyMatch(x -> account1.getId().equals(x.getId())));
    assertTrue(accounts.stream().anyMatch(x -> account2.getId().equals(x.getId())));
  }

}
