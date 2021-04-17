package com.ymmihw.spring.data.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import com.ymmihw.spring.data.jpa.model.Characteristic;
import com.ymmihw.spring.data.jpa.model.Item;
import com.ymmihw.spring.data.jpa.repository.CharacteristicsRepository;
import com.ymmihw.spring.data.jpa.repository.ItemRepository;

@DataJpaTest
@SpringBootTest
@Sql(scripts = "/entitygraph-data.sql")
public class EntityGraphIntegrationTest {

  @Autowired
  private ItemRepository itemRepo;

  @Autowired
  private CharacteristicsRepository characteristicsRepo;

  @Test
  public void givenEntityGraph_whenCalled_shouldRetrunDefinedFields() {
    Item item = itemRepo.findByName("Table");
    assertThat(item.getId()).isEqualTo(1L);
  }

  @Test
  public void givenAdhocEntityGraph_whenCalled_shouldRetrunDefinedFields() {
    Characteristic characteristic = characteristicsRepo.findByType("Rigid");
    assertThat(characteristic.getId()).isEqualTo(1L);
  }
}
