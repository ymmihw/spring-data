package com.ymmihw.hibernateenvers.persist.hibernate.service;

import com.ymmihw.hibernateenvers.model.Foo;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;

public interface IFooAuditableService extends IFooService, IAuditOperations<Foo> {
    //
}
