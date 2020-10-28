package com.ymmihw.spring.data.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories
@Configuration
@EnableTransactionManagement
public class AppConfig {
}
