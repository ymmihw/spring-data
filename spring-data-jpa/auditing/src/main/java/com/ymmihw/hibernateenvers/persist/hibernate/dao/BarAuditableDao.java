package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.AbstractHibernateAuditableDao;

@Repository
public class BarAuditableDao extends AbstractHibernateAuditableDao<Bar> implements IBarAuditableDao {

    public BarAuditableDao() {
        super();

        setClazz(Bar.class);
    }

    // API

    @Override
    public List<Bar> getRevisions() {
        final List<Bar> resultList = super.getRevisions();
        for (final Bar bar : resultList) {
            bar.getFooSet()
               .size(); // force FooSet initialization
        }
        return resultList;
    }

}