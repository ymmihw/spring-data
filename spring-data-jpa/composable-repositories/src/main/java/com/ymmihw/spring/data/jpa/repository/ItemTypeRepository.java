package com.ymmihw.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.ItemType;

@Repository
public interface ItemTypeRepository
    extends JpaRepository<ItemType, Long>, CustomItemTypeRepository, CustomItemRepository {
}
