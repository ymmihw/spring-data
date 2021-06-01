package com.ymmihw.jpa.auditing.persist.jpa.dao;

import org.springframework.stereotype.Repository;
import com.ymmihw.jpa.auditing.model.Bar;

@Repository
public class BarJpaDao extends AbstractJpaDao<Bar> implements IBarDao {

    public BarJpaDao() {
        super();

        setClazz(Bar.class);
    }

}
