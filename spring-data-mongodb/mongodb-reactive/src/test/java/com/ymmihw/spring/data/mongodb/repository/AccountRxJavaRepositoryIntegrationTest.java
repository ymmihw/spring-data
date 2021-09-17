package com.ymmihw.spring.data.mongodb.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ymmihw.spring.data.mongodb.BaseTest;
import com.ymmihw.spring.data.mongodb.model.Account;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AccountRxJavaRepositoryIntegrationTest extends BaseTest {

  @Autowired
  AccountRxJavaRepository repository;

  @BeforeEach
  public void before() {
    repository.deleteAll().blockingGet();
  }

  @Test
  public void givenValue_whenFindAllByValue_thenFindAccounts() throws InterruptedException {
    double value = 12.7;
    repository.save(new Account(null, "bruno", value)).blockingGet();
    Observable<Account> accountObservable = repository.findAllByValue(value);

    accountObservable.test().await().assertComplete().assertValueAt(0, account -> {
      assertEquals("bruno", account.getOwner());
      assertEquals(Double.valueOf(value), account.getValue());
      return true;
    });

  }

  @Test
  public void givenOwner_whenFindFirstByOwner_thenFindAccount() throws InterruptedException {
    double value = 12.6;
    repository.save(new Account(null, "bruno" + value, value)).blockingGet();
    Single<Account> accountSingle = repository.findFirstByOwner(Single.just("bruno" + value));

    accountSingle.test().await().assertComplete().assertValueAt(0, account -> {
      assertEquals("bruno" + value, account.getOwner());
      assertEquals(Double.valueOf(value), account.getValue());
      assertNotNull(account.getId());
      return true;
    });

  }

}
