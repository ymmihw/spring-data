package com.ymmihw.hibernateenvers.persist.hibernate.dao;

import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;

public interface IBarAuditableDao extends IBarDao, IAuditOperations<Bar> {
    //
}
