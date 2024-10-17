package br.com.reserva.reserva.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class ContaRDriverConfigurations {

    @Bean(name = "rDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.r")
    public DataSource rDataSource() {
        return new DriverManagerDataSource();
    }
    
}
