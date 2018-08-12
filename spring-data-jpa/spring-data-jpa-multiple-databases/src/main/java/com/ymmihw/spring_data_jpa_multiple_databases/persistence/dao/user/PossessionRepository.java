package com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.user.Possession;

public interface PossessionRepository extends JpaRepository<Possession, Long> {

}
