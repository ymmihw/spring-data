package com.ymmihw.hibernateenvers.persist.hibernate.service;

import com.ymmihw.hibernateenvers.model.Bar;
import com.ymmihw.hibernateenvers.persist.common.dao.IAuditOperations;

public interface IBarAuditableService extends IBarService, IAuditOperations<Bar> {

}
