package com.ymmihw.spring.data.jpa;

import org.hibernate.EmptyInterceptor;
import org.springframework.stereotype.Component;

@Component
public class HibernateInterceptor extends EmptyInterceptor {
  private static final long serialVersionUID = 1L;

  @Override
  public String onPrepareStatement(String sql) {
    Long formId = RequestContextHolder.getFormId();
    if (formId != null) {
      return sql.replaceAll("form_entry", "form_" + formId + "_entry");
    }
    return sql;
  }

}
