package com.ymmihw.hibernateenvers.persist.hibernate.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;
import com.ymmihw.hibernateenvers.persist.common.dao.IOperations;

@Transactional(value = "hibernateTransactionManager")
public abstract class AbstractHibernateAuditableService<T extends Serializable> extends AbstractHibernateService<T> implements IOperations<T>, IAuditOperations<T> {

    @Override
    public List<T> getEntitiesAtRevision(final Number revision) {
        return getAuditableDao().getEntitiesAtRevision(revision);
    }

    @Override
    public List<T> getEntitiesModifiedAtRevision(final Number revision) {
        return getAuditableDao().getEntitiesModifiedAtRevision(revision);
    }

    @Override
    public List<T> getRevisions() {
        return getAuditableDao().getRevisions();
    }

    abstract protected IAuditOperations<T> getAuditableDao();

}
