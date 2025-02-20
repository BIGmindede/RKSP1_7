package com.example.RKSP_7;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DbSchemaInitOnStartup {
    @Bean
    ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory")
                                             ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        ResourceDatabasePopulator resource = new ResourceDatabasePopulator(new
                ClassPathResource("init.sql"));
        initializer.setDatabasePopulator(resource);
        return initializer; }
}