package com.ymmihw.spring.data.jpa.query.annotation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.spring.data.jpa.query.annotation.Applicatoin;
import com.ymmihw.spring.data.jpa.query.annotation.model.User;


@SpringBootTest(classes = Applicatoin.class)
@DirtiesContext
public class UserRepositoryIntegrationTest extends UserRepositoryCommon {

  @Test
  @Transactional
  public void givenUsersInDBWhenUpdateStatusForNameModifyingQueryAnnotationNativeThenModifyMatchingUsers() {
    userRepository.save(new User("SAMPLE", USER_EMAIL, ACTIVE_STATUS));
    userRepository.save(new User("SAMPLE1", USER_EMAIL2, ACTIVE_STATUS));
    userRepository.save(new User("SAMPLE", USER_EMAIL3, ACTIVE_STATUS));
    userRepository.save(new User("SAMPLE3", USER_EMAIL4, ACTIVE_STATUS));
    userRepository.flush();

    int updatedUsersSize =
        userRepository.updateUserSetStatusForNameNative(INACTIVE_STATUS, "SAMPLE");

    assertThat(updatedUsersSize).isEqualTo(2);
  }
}
