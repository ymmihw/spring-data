package com.ymmihw.spring.data.mongodb.iac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ymmihw.spring.data.mongodb.iac.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class IndexTest extends BaseTest {
  @Test
  public void createIndexProgrammatically() {
    mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("name", Direction.DESC));
  }
}
