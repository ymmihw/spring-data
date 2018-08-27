package com.ymmihw.spring.data.jpa;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.ymmihw.spring.data.jpa.domain.Item;
import com.ymmihw.spring.data.jpa.domain.ItemType;
import com.ymmihw.spring.data.jpa.domain.Location;
import com.ymmihw.spring.data.jpa.domain.Store;
import com.ymmihw.spring.data.jpa.repository.ItemTypeRepository;
import com.ymmihw.spring.data.jpa.repository.LocationRepository;
import com.ymmihw.spring.data.jpa.repository.StoreRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Application.class})
public class JpaRepositoriesIntegrationTest {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ItemTypeRepository compositeRepository;

  @Test
  public void whenSaveLocation_ThenGetSameLocation() {
    Location location = new Location();
    location.setId(100L);
    location.setCountry("Country H");
    location.setCity("City Hundred");
    location = locationRepository.saveAndFlush(location);

    Location otherLocation = locationRepository.getOne(location.getId());
    assertEquals("Country H", otherLocation.getCountry());
    assertEquals("City Hundred", otherLocation.getCity());

    locationRepository.delete(otherLocation);
  }

  @Test
  public void givenLocationId_whenFindStores_thenGetStores() {
    List<Store> stores = storeRepository.findStoreByLocationId(1L);
    assertEquals(1, stores.size());
  }

  @Test
  public void givenItemTypeId_whenDeleted_ThenItemTypeDeleted() {
    Optional<ItemType> itemType = compositeRepository.findById(1L);
    assertTrue(itemType.isPresent());
    compositeRepository.deleteCustom(itemType.get());
    itemType = compositeRepository.findById(1L);
    assertFalse(itemType.isPresent());
  }

  @Test
  public void givenItemId_whenUsingCustomRepo_ThenDeleteAppropriateEntity() {
    Item item = compositeRepository.findItemById(1L);
    assertNotNull(item);
    compositeRepository.deleteCustom(item);
    item = compositeRepository.findItemById(1L);
    assertNull(item);
  }

  @Test
  public void givenItemAndItemType_WhenAmbiguousDeleteCalled_ThenItemTypeDeletedAndNotItem() {
    Optional<ItemType> itemType = compositeRepository.findById(1L);
    assertTrue(itemType.isPresent());
    Item item = compositeRepository.findItemById(2L);
    assertNotNull(item);

    compositeRepository.findThenDelete(1L);
    Optional<ItemType> sameItemType = compositeRepository.findById(1L);
    assertFalse(sameItemType.isPresent());
    Item sameItem = compositeRepository.findItemById(2L);
    assertNotNull(sameItem);
  }

}
