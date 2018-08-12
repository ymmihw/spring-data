package com.ymmihw.spring.data.jpa.advanced.tagging.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {


  @Query("SELECT s FROM Student s JOIN s.skillTags t WHERE t.name = LOWER(:tagName) AND t.value > :tagValue")
  List<Student> retrieveByNameFilterByMinimumSkillTag(@Param("tagName") String tagName,
      @Param("tagValue") int tagValue);

  @Query("SELECT s FROM Student s JOIN s.kvTags t WHERE t.key = LOWER(:key)")
  List<Student> retrieveByKeyTag(@Param("key") String key);

}
