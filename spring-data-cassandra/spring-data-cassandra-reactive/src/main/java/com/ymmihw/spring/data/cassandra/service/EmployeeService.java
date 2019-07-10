package com.ymmihw.spring.data.cassandra.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ymmihw.spring.data.cassandra.model.Employee;
import com.ymmihw.spring.data.cassandra.repository.EmployeeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {

  @Autowired
  EmployeeRepository employeeRepository;

  public void initializeEmployees(List<Employee> employees) {
    Flux<Employee> savedEmployees = employeeRepository.saveAll(employees);
    savedEmployees.subscribe();
  }

  public Flux<Employee> getAllEmployees() {
    Flux<Employee> employees = employeeRepository.findAll();
    return employees;
  }

  public Flux<Employee> getEmployeesFilterByAge(int age) {
    return employeeRepository.findByAgeGreaterThan(age);
  }

  public Mono<Employee> getEmployeeById(int id) {
    return employeeRepository.findById(id);
  }
}
