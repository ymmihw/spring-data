package com.ymmihw.hibernateenvers.persist.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;
import com.ymmihw.hibernateenvers.persist.common.dao.IOperations;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IFooAuditableDao;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IFooDao;

@Service
public class FooAuditableService extends AbstractHibernateAuditableService<Foo> implements IFooAuditableService {

    @Autowired
    @Qualifier("fooDao")
    private IFooDao dao;

    @Autowired
    @Qualifier("fooAuditableDao")
    private IFooAuditableDao auditDao;

    public FooAuditableService() {
        super();
    }

    // API

    @Override
    protected IOperations<Foo> getDao() {
        return dao;
    }

    @Override
    protected IAuditOperations<Foo> getAuditableDao() {
        return auditDao;
    }

}
