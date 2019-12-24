package com.ymmihw.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring.data.jpa.model.Characteristic;

public interface CharacteristicsRepository extends JpaRepository<Characteristic, Long> {

  @EntityGraph(attributePaths = {"item"})
  Characteristic findByType(String type);

}
