package com.ymmihw.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ymmihw.spring.data.jpa.domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
