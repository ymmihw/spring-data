package com.ymmihw.spring.data.mongodb.iac;

import com.ymmihw.spring.data.mongodb.iac.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.Index;

public class IndexTest extends BaseTest {
  @Test
  public void createIndexProgrammatically() {
    mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("name", Direction.DESC));
  }
}
