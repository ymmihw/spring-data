package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import org.springframework.stereotype.Repository;
import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.AbstractHibernateDao;

@Repository
public class FooDao extends AbstractHibernateDao<Foo> implements IFooDao {

    public FooDao() {
        super();

        setClazz(Foo.class);
    }

    // API

}
