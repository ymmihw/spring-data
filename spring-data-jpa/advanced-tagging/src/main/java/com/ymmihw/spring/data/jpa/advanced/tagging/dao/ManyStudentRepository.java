package com.ymmihw.spring.data.jpa.advanced.tagging.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.ManyStudent;

public interface ManyStudentRepository extends JpaRepository<ManyStudent, Long> {
  List<ManyStudent> findByManyTags_Name(String name);
}
