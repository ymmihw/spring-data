package com.ymmihw.spring.data.keyvalue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import com.ymmihw.spring.data.keyvalue.repositories.EmployeeRepository;
import com.ymmihw.spring.data.keyvalue.services.EmployeeService;
import com.ymmihw.spring.data.keyvalue.vo.Employee;


@SpringBootTest(classes = SpringDataKeyValueApplication.class)
@TestMethodOrder(MethodName.class)
public class EmployeeServicesWithRepositoryIntegrationTest {

  @Autowired
  @Qualifier("employeeServicesWithRepository")
  EmployeeService employeeService;

  @Autowired
  EmployeeRepository employeeRepository;

  static Employee employee1;

  @BeforeAll
  public static void setUp() {
    employee1 = new Employee(1, "Karan", "IT", "5000");
  }

  @Test
  public void test1_whenEmployeeSaved_thenEmployeeIsAddedToMap() {
    employeeService.save(employee1);
    assertEquals(employeeRepository.findById(1).get(), employee1);
  }

  @Test
  public void test2_whenEmployeeGet_thenEmployeeIsReturnedFromMap() {
    Employee employeeFetched = employeeService.get(1).get();
    assertEquals(employeeFetched, employee1);
  }

  @Test
  public void test3_whenEmployeesFetched_thenEmployeesAreReturnedFromMap() {
    List<Employee> employees = (List<Employee>) employeeService.fetchAll();
    assertEquals(employees.size(), 1);
    assertEquals(employees.get(0), employee1);
  }

  @Test
  public void test4_whenEmployeeUpdated_thenEmployeeIsUpdatedToMap() {
    employee1.setName("Pawan");
    employeeService.update(employee1);
    assertEquals(employeeRepository.findById(1).get().getName(), "Pawan");
  }

  @Test
  public void test5_whenEmployeeDeleted_thenEmployeeIsRemovedMap() {
    employeeService.delete(1);
    assertEquals(employeeRepository.findById(1).isPresent(), false);
  }

}
