package com.example.seabattleserver.config;

import java.util.Arrays;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Profile("!functionalTest")
public class PgConfig {
    @Value("${seabattle.postgresql.url}")
    private String url;

    @Value("${seabattle.postgresql.username}")
    private String username;

    @Value("${seabattle.postgresql.password}")
    private String password;

    @Value("${seabattle.postgresql.driver:org.postgresql.Driver}")
    private String driver;

    @Value("${seabattle.postgresql.max.pool.size:10}")
    private int maxPoolSize;

    @Value("${seabattle.postgresql.properties}")
    private String properties;



    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driver)
                .build();
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
