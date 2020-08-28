package com.ymmihw.spring.data.jpa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class FormEntryServiceImpl {
  private final FormEntryRepo formEntryRepo;

  public FormEntry findById(Long id) {
    return formEntryRepo.findById(id).orElse(null);
  }
}
