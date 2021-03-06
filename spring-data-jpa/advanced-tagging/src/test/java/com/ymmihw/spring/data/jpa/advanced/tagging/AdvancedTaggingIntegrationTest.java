package com.ymmihw.spring.data.jpa.advanced.tagging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.spring.data.jpa.advanced.tagging.dao.ManyStudentRepository;
import com.ymmihw.spring.data.jpa.advanced.tagging.dao.ManyTagRepository;
import com.ymmihw.spring.data.jpa.advanced.tagging.dao.StudentRepository;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.KVTag;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.ManyStudent;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.ManyTag;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.SkillTag;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.Student;

@SpringBootTest
@ContextConfiguration(classes = {Applicatoin.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class AdvancedTaggingIntegrationTest {
  @Resource
  private StudentRepository studentRepository;

  @Resource
  private ManyStudentRepository manyStudentRepository;

  @Resource
  private ManyTagRepository manyTagRepository;

  @Test
  public void givenStudentWithSkillTags_whenSave_thenGetByNameAndSkillTag() {
    Student student = new Student(1, "Will");
    SkillTag skill1 = new SkillTag("java", 5);
    student.setSkillTags(Arrays.asList(skill1));
    studentRepository.save(student);

    Student student2 = new Student(2, "Joe");
    SkillTag skill2 = new SkillTag("java", 1);
    student2.setSkillTags(Arrays.asList(skill2));
    studentRepository.save(student2);

    List<Student> students = studentRepository.retrieveByNameFilterByMinimumSkillTag("java", 3);
    assertEquals(1, students.size(), "size incorrect");
  }

  @Test
  public void givenStudentWithKVTags_whenSave_thenGetByTagOk() {
    Student student = new Student(0, "John");
    student.setKVTags(Arrays.asList(new KVTag("department", "computer science")));
    studentRepository.save(student);

    Student student2 = new Student(1, "James");
    student2.setKVTags(Arrays.asList(new KVTag("department", "humanities")));
    studentRepository.save(student2);

    List<Student> students = studentRepository.retrieveByKeyTag("department");
    assertEquals(2, students.size(), "size incorrect");
  }

  @Test
  public void givenStudentWithManyTags_whenSave_theyGetByTagOk() {
    ManyTag tag = new ManyTag("full time");
    manyTagRepository.save(tag);

    ManyStudent student = new ManyStudent("John");
    student.setManyTags(Collections.singleton(tag));
    manyStudentRepository.save(student);

    List<ManyStudent> students = manyStudentRepository.findByManyTags_Name("full time");
    assertEquals(1, students.size(), "size incorrect");
  }
}
