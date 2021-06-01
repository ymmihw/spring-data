package com.ymmihw.hibernateenvers.persist.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.IOperations;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IFooDao;

@Service
public class FooService extends AbstractHibernateService<Foo> implements IFooService {

    @Autowired
    @Qualifier("fooDao")
    private IFooDao dao;

    public FooService() {
        super();
    }

    // API

    @Override
    protected IOperations<Foo> getDao() {
        return dao;
    }

}
