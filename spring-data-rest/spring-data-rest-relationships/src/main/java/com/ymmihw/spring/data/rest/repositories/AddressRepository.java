package com.ymmihw.spring.data.rest.repositories;

import org.springframework.data.repository.CrudRepository;
import com.ymmihw.spring.data.rest.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

}
