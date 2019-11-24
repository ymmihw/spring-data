package com.ymmihw.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring.data.jpa.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

  @EntityGraph(value = "Item.characteristics", type = EntityGraphType.FETCH)
  Item findByName(String name);
}
