package com.ymmihw.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FormEntryRepo
    extends JpaRepository<FormEntry, Long>, JpaSpecificationExecutor<FormEntry> {

}
