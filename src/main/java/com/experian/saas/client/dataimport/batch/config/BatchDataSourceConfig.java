package com.experian.saas.client.dataimport.batch.config;

import com.experian.saas.client.dataimport.batch.components.DatabaseConfigParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class BatchDataSourceConfig {

    @Autowired
    private DatabaseConfigParams databaseConfigParams;

    @Bean
    @Primary
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        return builder.build();
    }

    @Bean(name = "postgresDataSource")
    public DataSource postgresDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(databaseConfigParams.getPostgresUrl());
        dataSourceBuilder.username(databaseConfigParams.getPostgresUsername());
        dataSourceBuilder.password(databaseConfigParams.getPostgresPassword());
        dataSourceBuilder.driverClassName(databaseConfigParams.getDriverClassName());
        return dataSourceBuilder.build();
    }

    @Bean(name = "postgresJdbcTemplate")
    public JdbcTemplate postgresJdbcTemplate() {
        return new JdbcTemplate(postgresDataSource());
    }
}