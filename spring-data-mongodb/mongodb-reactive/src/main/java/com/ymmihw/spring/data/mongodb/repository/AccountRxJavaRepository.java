package com.ymmihw.spring.data.mongodb.repository;

import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.mongodb.model.Account;
import io.reactivex.Observable;
import io.reactivex.Single;

@Repository
public interface AccountRxJavaRepository extends RxJava2CrudRepository<Account, String> {

  public Observable<Account> findAllByValue(Double value);

  public Single<Account> findFirstByOwner(Single<String> owner);
}
