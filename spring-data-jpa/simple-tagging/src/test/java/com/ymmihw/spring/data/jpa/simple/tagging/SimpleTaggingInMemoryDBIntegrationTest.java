package com.ymmihw.spring.data.jpa.simple.tagging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.spring.data.jpa.simple.tagging.config.JpaConfig;
import com.ymmihw.spring.data.jpa.simple.tagging.dao.StudentRepository;
import com.ymmihw.spring.data.jpa.simple.tagging.model.Student;

@SpringBootTest
@ContextConfiguration(classes = {JpaConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SimpleTaggingInMemoryDBIntegrationTest {

  @Resource
  private StudentRepository studentRepository;

  private static final long ID = 1;
  private static final String NAME = "john";

  @Test
  public void givenStudent_whenSave_thenGetOk() {
    Student student = new Student(ID, NAME);
    studentRepository.save(student);

    Optional<Student> student2 = studentRepository.findById(ID);
    assertEquals("name incorrect", NAME, student2.get().getName());
  }

  @Test
  public void givenStudentWithTags_whenSave_thenGetByTagOk() {
    Student student = new Student(ID, NAME);
    student.setTags(Arrays.asList("full time", "computer science"));
    studentRepository.save(student);

    Student student2 = studentRepository.retrieveByTag("full time").get(0);
    assertEquals("name incorrect", NAME, student2.getName());
  }

  @Test
  public void givenMultipleStudentsWithTags_whenSave_thenGetByTagReturnsCorrectCount() {
    Student student = new Student(0, "Larry");
    student.setTags(Arrays.asList("full time", "computer science"));
    studentRepository.save(student);

    Student student2 = new Student(1, "Curly");
    student2.setTags(Arrays.asList("part time", "rocket science"));
    studentRepository.save(student2);

    Student student3 = new Student(2, "Moe");
    student3.setTags(Arrays.asList("full time", "philosophy"));
    studentRepository.save(student3);

    Student student4 = new Student(3, "Shemp");
    student4.setTags(Arrays.asList("part time", "mathematics"));
    studentRepository.save(student4);

    List<Student> students = studentRepository.retrieveByTag("full time");
    assertEquals(2, students.size(), "size incorrect");
  }

  @Test
  public void givenStudentWithTags_whenSave_thenGetByNameAndTagOk() {
    Student student = new Student(ID, NAME);
    student.setTags(Arrays.asList("full time", "computer science"));
    studentRepository.save(student);

    Student student2 = studentRepository.retrieveByNameFilterByTag("John", "full time").get(0);
    assertEquals("name incorrect", NAME, student2.getName());
  }

}
