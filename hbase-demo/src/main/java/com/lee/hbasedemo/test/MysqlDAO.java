package com.lee.hbasedemo.test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MysqlDAO {
    @Bean(name = "datacenterDataSource")
    @Qualifier("datacenterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.datacenter")
    public DataSource datacenterDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "datacenterJdbcTemplate")
    public JdbcTemplate getJdbcTemplateExercise(
            @Qualifier("datacenterDataSource") DataSource dataSource) {
        System.out.println("datacenterDataSource" + dataSource);
        return new JdbcTemplate(dataSource);
    }
}
