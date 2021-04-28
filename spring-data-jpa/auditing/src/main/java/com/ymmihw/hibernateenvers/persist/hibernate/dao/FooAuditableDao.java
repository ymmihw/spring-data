package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import org.springframework.stereotype.Repository;
import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.AbstractHibernateAuditableDao;

@Repository
public class FooAuditableDao extends AbstractHibernateAuditableDao<Foo> implements IFooAuditableDao {

    public FooAuditableDao() {
        super();

        setClazz(Foo.class);
    }

    // API

}
