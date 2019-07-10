package com.ymmihw.spring.data.mongodb.listener;


import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import com.ymmihw.spring.data.mongodb.model.User;
import com.ymmihw.spring.data.mongodb.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserModelListener extends AbstractMongoEventListener<User> {

  private final SequenceGeneratorService sequenceGenerator;

  @Override
  public void onBeforeConvert(BeforeConvertEvent<User> event) {
    if (event.getSource().getId() < 1) {
      event.getSource().setId(sequenceGenerator.generateSequence(User.SEQUENCE_NAME));
    }
  }
}
