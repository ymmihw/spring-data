package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import org.springframework.stereotype.Repository;
import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.AbstractHibernateDao;

@Repository
public class BarDao extends AbstractHibernateDao<Bar> implements IBarDao {

    public BarDao() {
        super();

        setClazz(Bar.class);
    }

    // API

}
