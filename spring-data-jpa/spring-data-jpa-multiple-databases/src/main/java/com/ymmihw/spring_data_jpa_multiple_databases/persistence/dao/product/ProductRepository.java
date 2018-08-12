package com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.product;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
