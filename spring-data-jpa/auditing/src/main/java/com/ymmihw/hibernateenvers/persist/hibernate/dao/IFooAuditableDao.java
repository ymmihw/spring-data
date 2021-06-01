package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;

public interface IFooAuditableDao extends IFooDao, IAuditOperations<Foo> {
    //
}