package com.ymmihw.spring.data.jpa.advanced.tagging.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring.data.jpa.advanced.tagging.model.ManyTag;

public interface ManyTagRepository extends JpaRepository<ManyTag, Long> {
}
