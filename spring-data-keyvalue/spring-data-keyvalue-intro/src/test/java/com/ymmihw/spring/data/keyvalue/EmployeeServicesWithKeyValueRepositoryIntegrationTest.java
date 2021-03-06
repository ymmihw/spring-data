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
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import com.ymmihw.spring.data.keyvalue.services.EmployeeService;
import com.ymmihw.spring.data.keyvalue.vo.Employee;

@SpringBootTest(classes = SpringDataKeyValueApplication.class)
@TestMethodOrder(MethodName.class)
public class EmployeeServicesWithKeyValueRepositoryIntegrationTest {

  @Autowired
  @Qualifier("employeeServicesWithKeyValueTemplate")
  EmployeeService employeeService;

  @Autowired
  @Qualifier("keyValueTemplate")
  KeyValueTemplate keyValueTemplate;

  static Employee employee1;

  static Employee employee2;

  @BeforeAll
  public static void setUp() {
    employee1 = new Employee(1, "Karan", "IT", "5000");
    employee2 = new Employee(2, "Jack", "HR", "2000");
  }

  @Test
  public void test1_whenEmployeeSaved_thenEmployeeIsAddedToMap() {
    employeeService.save(employee1);
    assertEquals(keyValueTemplate.findById(1, Employee.class).get(), employee1);
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
    assertEquals(keyValueTemplate.findById(1, Employee.class).get().getName(), "Pawan");
  }

  @Test
  public void test5_whenSortedEmployeesFetched_thenEmployeesAreReturnedFromMapInOrder() {
    employeeService.save(employee2);
    List<Employee> employees = (List<Employee>) employeeService.getSortedListOfEmployeesBySalary();
    assertEquals(employees.size(), 2);
    assertEquals(employees.get(0), employee1);
    assertEquals(employees.get(1), employee2);
  }

  @Test
  public void test6_whenEmployeeDeleted_thenEmployeeIsRemovedMap() {
    employeeService.delete(1);
    assertEquals(keyValueTemplate.findById(1, Employee.class).isPresent(), false);
  }



}
