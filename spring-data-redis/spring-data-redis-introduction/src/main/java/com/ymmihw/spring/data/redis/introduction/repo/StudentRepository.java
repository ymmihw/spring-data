package com.ymmihw.spring.data.redis.introduction.repo;

import java.util.Map;
import com.ymmihw.spring.data.redis.introduction.model.Student;

public interface StudentRepository {

  void saveStudent(Student person);

  void updateStudent(Student student);

  Student findStudent(String id);

  Map<Object, Object> findAllStudents();

  void deleteStudent(String id);
}
