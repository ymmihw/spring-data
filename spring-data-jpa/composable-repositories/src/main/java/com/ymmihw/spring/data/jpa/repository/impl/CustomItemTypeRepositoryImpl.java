package com.ymmihw.spring.data.jpa.repository.impl;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.ItemType;
import com.ymmihw.spring.data.jpa.repository.CustomItemTypeRepository;

@Repository
public class CustomItemTypeRepositoryImpl implements CustomItemTypeRepository {

  @Autowired
  private EntityManager entityManager;

  @Override
  public void deleteCustom(ItemType itemType) {
    entityManager.remove(itemType);
  }

  @Override
  public void findThenDelete(Long id) {
    ItemType itemTypeToDelete = entityManager.find(ItemType.class, id);
    entityManager.remove(itemTypeToDelete);
  }
}
