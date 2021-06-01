package com.ymmihw.hibernateenvers.persist.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;
import com.ymmihw.hibernateenvers.persist.common.dao.IOperations;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IBarAuditableDao;
import com.ymmihw.hibernateenvers.persist.hibernate.dao.IBarDao;

@Service
public class BarAuditableService extends AbstractHibernateAuditableService<Bar> implements IBarAuditableService {

    @Autowired
    @Qualifier("barDao")
    private IBarDao dao;

    @Autowired
    @Qualifier("barAuditableDao")
    private IBarAuditableDao auditDao;

    public BarAuditableService() {
        super();
    }

    // API

    @Override
    protected IOperations<Bar> getDao() {
        return dao;
    }

    @Override
    protected IAuditOperations<Bar> getAuditableDao() {
        return auditDao;
    }

}
