package com.ymmihw.spring.data.redis.introduction.repo;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.redis.introduction.model.Student;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

  private static final String KEY = "Student";

  private RedisTemplate<String, Object> redisTemplate;
  private HashOperations<String, Object, Object> hashOperations;

  @Autowired
  public StudentRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  private void init() {
    hashOperations = redisTemplate.opsForHash();
  }

  @Override
  public void saveStudent(final Student student) {
    hashOperations.put(KEY, student.getId(), student);
  }

  @Override
  public void updateStudent(final Student student) {
    hashOperations.put(KEY, student.getId(), student);
  }

  @Override
  public Student findStudent(final String id) {
    return (Student) hashOperations.get(KEY, id);
  }

  @Override
  public Map<Object, Object> findAllStudents() {
    return hashOperations.entries(KEY);
  }

  @Override
  public void deleteStudent(final String id) {
    hashOperations.delete(KEY, id);
  }
}
