package com.ymmihw.spring_data_jpa_multiple_databases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.spring_data_jpa_multiple_databases.config.ProductConfig;
import com.ymmihw.spring_data_jpa_multiple_databases.config.UserConfig;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.product.ProductRepository;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.user.PossessionRepository;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.user.UserRepository;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.product.Product;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.user.Possession;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.user.User;

@SpringBootTest
@ContextConfiguration(classes = {UserConfig.class, ProductConfig.class})
@EnableTransactionManagement
public class JpaMultipleDBIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PossessionRepository possessionRepository;

  @Autowired
  private ProductRepository productRepository;

  // tests

  @Test
  @Transactional("userTransactionManager")
  public void whenCreatingUser_thenCreated() {
    User user = new User();
    user.setName("John");
    user.setEmail("john@test.com");
    user.setAge(20);
    Possession p = new Possession("sample");
    p = possessionRepository.save(p);
    user.setPossessionList(Arrays.asList(p));
    user = userRepository.save(user);
    final User result = userRepository.getOne(user.getId());
    assertNotNull(result);
    System.out.println(result.getPossessionList());
    assertTrue(result.getPossessionList().size() == 1);
  }

  @Test
  @Transactional("userTransactionManager")
  public void whenCreatingUsersWithSameEmail_thenRollback() {
    User user1 = new User();
    user1.setName("John");
    user1.setEmail("john@test.com");
    user1.setAge(20);
    user1 = userRepository.save(user1);
    assertNotNull(userRepository.getOne(user1.getId()));

    User user2 = new User();
    user2.setName("Tom");
    user2.setEmail("john@test.com");
    user2.setAge(10);
    try {
      user2 = userRepository.save(user2);
      userRepository.flush();
      fail("DataIntegrityViolationException should be thrown!");
    } catch (final DataIntegrityViolationException e) {
      // Expected
    } catch (final Exception e) {
      fail("DataIntegrityViolationException should be thrown, instead got: " + e);
    }
  }

  @Test
  @Transactional("productTransactionManager")
  public void whenCreatingProduct_thenCreated() {
    Product product = new Product();
    product.setName("Book");
    product.setId(2);
    product.setPrice(20);
    product = productRepository.save(product);

    assertNotNull(productRepository.getOne(product.getId()));
  }

}
