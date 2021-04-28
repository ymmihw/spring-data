package com.ymmihw.springdatajpa.auditing;

import java.util.Optional;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class SpringDataJpaApp {
  @Bean
  public AuditorAware<String> aware() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication).filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal).map(User.class::cast).map(User::getUsername);
  }
}
