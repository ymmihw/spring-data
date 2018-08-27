package com.ymmihw.spring.data.jpa.repository;

import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.Item;

@Repository
public interface CustomItemRepository {

  void deleteCustom(Item entity);

  Item findItemById(Long id);

  void findThenDelete(Long id);
}
