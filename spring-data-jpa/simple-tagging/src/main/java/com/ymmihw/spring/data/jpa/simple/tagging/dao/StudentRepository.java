package com.ymmihw.spring.data.jpa.simple.tagging.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ymmihw.spring.data.jpa.simple.tagging.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

  @Query("SELECT s FROM Student s JOIN s.tags t WHERE t = LOWER(:tag)")
  List<Student> retrieveByTag(@Param("tag") String tag);

  @Query("SELECT s FROM Student s JOIN s.tags t WHERE s.name = LOWER(:name) AND t = LOWER(:tag)")
  List<Student> retrieveByNameFilterByTag(@Param("name") String name, @Param("tag") String tag);

}
