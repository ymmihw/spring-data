package com.ymmihw.jpa.auditing.persist.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ymmihw.jpa.auditing.model.Bar;
import com.ymmihw.jpa.auditing.persist.common.dao.IOperations;
import com.ymmihw.jpa.auditing.persist.common.service.AbstractJpaService;
import com.ymmihw.jpa.auditing.persist.jpa.dao.IBarDao;

@Service
public class BarJpaService extends AbstractJpaService<Bar> implements IBarService {

    @Autowired
    @Qualifier("barJpaDao")
    private IBarDao dao;

    public BarJpaService() {
        super();
    }

    // API

    @Override
    protected IOperations<Bar> getDao() {
        return dao;
    }

}