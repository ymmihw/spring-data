package com.ymmihw.spring.data.jpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findStoreByLocationId(Long locationId);
}
