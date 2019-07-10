package com.ymmihw.spring.data.cassandra.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import com.ymmihw.spring.data.cassandra.model.Employee;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveCassandraRepository<Employee, Integer> {
  @AllowFiltering
  Flux<Employee> findByAgeGreaterThan(int age);
}
