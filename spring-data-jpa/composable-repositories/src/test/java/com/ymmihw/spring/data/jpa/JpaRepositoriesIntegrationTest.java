package com.ymmihw.spring.data.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import com.ymmihw.spring.data.jpa.domain.Item;
import com.ymmihw.spring.data.jpa.domain.ItemType;
import com.ymmihw.spring.data.jpa.domain.Location;
import com.ymmihw.spring.data.jpa.domain.Store;
import com.ymmihw.spring.data.jpa.repository.ItemTypeRepository;
import com.ymmihw.spring.data.jpa.repository.LocationRepository;
import com.ymmihw.spring.data.jpa.repository.StoreRepository;

@DataJpaTest
@ContextConfiguration(classes = {Application.class})
public class JpaRepositoriesIntegrationTest {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ItemTypeRepository composedRepository;

  @Test
  public void whenSaveLocation_ThenGetSameLocation() {
    Location location = new Location();
    location.setId(100L);
    location.setCountry("Country H");
    location.setCity("City Hundred");
    location = locationRepository.saveAndFlush(location);

    Location otherLocation = locationRepository.getOne(location.getId());
    assertEquals(otherLocation.getCountry(), "Country H");
    assertEquals(otherLocation.getCity(), "City Hundred");

    locationRepository.delete(otherLocation);
  }

  @Test
  public void givenLocationId_whenFindStores_thenGetStores() {
    List<Store> stores = storeRepository.findStoreByLocationId(1L);
    assertEquals(1, stores.size());
  }

  @Test
  public void givenItemTypeId_whenDeleted_ThenItemTypeDeleted() {
    Optional<ItemType> itemType = composedRepository.findById(1L);
    assertTrue(itemType.isPresent());
    composedRepository.deleteCustom(itemType.get());
    itemType = composedRepository.findById(1L);
    assertFalse(itemType.isPresent());
  }

  @Test
  public void givenItemId_whenUsingCustomRepo_ThenDeleteAppropriateEntity() {
    Item item = composedRepository.findItemById(1L);
    assertNotNull(item);
    composedRepository.deleteCustom(item);
    item = composedRepository.findItemById(1L);
    assertNull(item);
  }

  @Test
  public void givenItemAndItemType_WhenAmbiguousDeleteCalled_ThenItemTypeDeletedAndNotItem() {
    Optional<ItemType> itemType = composedRepository.findById(1L);
    assertTrue(itemType.isPresent());
    Item item = composedRepository.findItemById(2L);
    assertNotNull(item);

    composedRepository.findThenDelete(1L);
    Optional<ItemType> sameItemType = composedRepository.findById(1L);
    assertFalse(sameItemType.isPresent());
    Item sameItem = composedRepository.findItemById(2L);
    assertNotNull(sameItem);
  }

  @Test
  public void givenItemAndItemTypeWhenDeleteThenItemTypeDeleted() {
    Optional<ItemType> itemType = composedRepository.findById(1L);
    assertTrue(itemType.isPresent());

    Item item = composedRepository.findItemById(2L);
    assertNotNull(item);

    composedRepository.findThenDelete(1L);
    Optional<ItemType> sameItemType = composedRepository.findById(1L);
    assertFalse(sameItemType.isPresent());

    Item sameItem = composedRepository.findItemById(2L);
    assertNotNull(sameItem);
  }

}
