package com.ymmihw.spring.data.neo4j.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.ymmihw.spring.data.neo4j.services"})
@EnableNeo4jRepositories(basePackages = "com.ymmihw.spring.data.neo4j.repository")
public class MovieDatabaseNeo4jTestConfiguration {}
