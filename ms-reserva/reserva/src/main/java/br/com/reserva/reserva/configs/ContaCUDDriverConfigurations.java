package br.com.reserva.reserva.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class ContaCUDDriverConfigurations {

    @Bean(name = "cudDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.cud")
    public DataSource cudDataSource() {
        return new DriverManagerDataSource();
    }
    
}
