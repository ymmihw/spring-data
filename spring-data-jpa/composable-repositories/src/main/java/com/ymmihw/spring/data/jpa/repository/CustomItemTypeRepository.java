package com.ymmihw.spring.data.jpa.repository;

import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.ItemType;

@Repository
public interface CustomItemTypeRepository {

  void deleteCustom(ItemType entity);

  void findThenDelete(Long id);
}
