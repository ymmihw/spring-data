package com.ymmihw.hibernateenvers.persist.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.IOperations;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IBarDao;

@Service
public class BarService extends AbstractHibernateService<Bar> implements IBarService {

    @Autowired
    @Qualifier("barDao")
    private IBarDao dao;

    public BarService() {
        super();
    }

    // API

    @Override
    protected IOperations<Bar> getDao() {
        return dao;
    }

}
