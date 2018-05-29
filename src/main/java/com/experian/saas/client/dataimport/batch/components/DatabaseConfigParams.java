package com.experian.saas.client.dataimport.batch.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfigParams {
    @Value("${postgres.datasource.url}")
    private String postgresUrl;

    @Value("${postgres.datasource.username}")
    private String postgresUsername;

    @Value("${postgres.datasource.password}")
    private String postgresPassword;

    @Value("${postgres.datasource.driverClassName}")
    private String driverClassName;

    public String getPostgresUrl() {
        return postgresUrl;
    }

    public String getPostgresUsername() {
        return postgresUsername;
    }

    public String getPostgresPassword() {
        return postgresPassword;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
