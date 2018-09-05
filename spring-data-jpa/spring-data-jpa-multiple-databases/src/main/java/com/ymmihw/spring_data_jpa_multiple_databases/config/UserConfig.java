package com.ymmihw.spring_data_jpa_multiple_databases.config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.google.common.base.Preconditions;

@Configuration
@PropertySource({"classpath:persistence-multiple-db.properties"})
@EnableJpaRepositories(
    basePackages = "com.ymmihw.spring_data_jpa_multiple_databases.persistence.dao.user",
    entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager")
public class UserConfig {
  @Autowired
  private Environment env;

  public UserConfig() {
    super();
  }

  //

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean userEntityManager() {
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(userDataSource());
    em.setPackagesToScan(
        new String[] {"com.ymmihw.spring_data_jpa_multiple_databases.persistence.model.user"});

    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    final HashMap<String, Object> properties = new HashMap<String, Object>();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
    properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Primary
  @Bean
  public DataSource userDataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource
        .setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
    dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("user.jdbc.url")));
    dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("jdbc.user")));
    dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("jdbc.pass")));

    return dataSource;
  }

  @Primary
  @Bean
  public PlatformTransactionManager userTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(userEntityManager().getObject());
    return transactionManager;
  }

}