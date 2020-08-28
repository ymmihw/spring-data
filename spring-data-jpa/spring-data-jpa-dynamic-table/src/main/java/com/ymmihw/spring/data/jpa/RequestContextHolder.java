package com.ymmihw.spring.data.jpa;

public class RequestContextHolder {
  private static ThreadLocal<Long> formId = new ThreadLocal<>();

  public static Long getFormId() {
    return formId.get();
  }

  public static void setFormId(Long formId) {
    RequestContextHolder.formId.set(formId);
  }
}
