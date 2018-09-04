package com.ymmihw.spring.data.keyvalue.services;

import java.util.Optional;
import com.ymmihw.spring.data.keyvalue.vo.Employee;

public interface EmployeeService {

  void save(Employee employee);

  Optional<Employee> get(Integer id);

  Iterable<Employee> fetchAll();

  void update(Employee employee);

  void delete(Integer id);

  Iterable<Employee> getSortedListOfEmployeesBySalary();

}
