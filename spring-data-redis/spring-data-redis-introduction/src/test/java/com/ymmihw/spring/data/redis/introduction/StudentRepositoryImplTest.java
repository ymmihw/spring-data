package com.ymmihw.spring.data.redis.introduction;

import com.ymmihw.spring.data.redis.RedisContainer;
import com.ymmihw.spring.data.redis.introduction.config.RedisConfig;
import com.ymmihw.spring.data.redis.introduction.model.Student;
import com.ymmihw.spring.data.redis.introduction.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@ContextConfiguration(
    classes = {RedisConfig.class},
    loader = AnnotationConfigContextLoader.class)
@TestPropertySource(locations = "classpath:/application.properties")
@Testcontainers
public class StudentRepositoryImplTest {
  @Container public static RedisContainer container = RedisContainer.getInstance();

  @Autowired private StudentRepository studentRepository;

  @Test
  public void whenSavingStudent_thenAvailableOnRetrieval() throws Exception {
    final Student student = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
    studentRepository.saveStudent(student);
    final Student retrievedStudent = studentRepository.findStudent(student.getId());
    assertEquals(student.getId(), retrievedStudent.getId());
  }

  @Test
  public void whenUpdatingStudent_thenAvailableOnRetrieval() throws Exception {
    final Student student = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
    studentRepository.saveStudent(student);
    student.setName("Richard Watson");
    studentRepository.saveStudent(student);
    final Student retrievedStudent = studentRepository.findStudent(student.getId());
    assertEquals(student.getName(), retrievedStudent.getName());
  }

  @Test
  public void whenSavingStudents_thenAllShouldAvailableOnRetrieval() throws Exception {
    final Student engStudent = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
    final Student medStudent = new Student("Med2015001", "Gareth Houston", Student.Gender.MALE, 2);
    studentRepository.saveStudent(engStudent);
    studentRepository.saveStudent(medStudent);
    final Map<Object, Object> retrievedStudent = studentRepository.findAllStudents();
    assertEquals(2, retrievedStudent.size());
  }

  @Test
  public void whenDeletingStudent_thenNotAvailableOnRetrieval() throws Exception {
    final Student student = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
    studentRepository.saveStudent(student);
    studentRepository.deleteStudent(student.getId());
    final Student retrievedStudent = studentRepository.findStudent(student.getId());
    assertNull(retrievedStudent);
  }
}
