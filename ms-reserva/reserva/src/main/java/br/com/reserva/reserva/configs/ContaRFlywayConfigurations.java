package br.com.reserva.reserva.configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ContaRFlywayConfigurations {

    @Bean(name = "flywayR")
    public Flyway flywayR(@Qualifier("rDataSource") DataSource rDataSource) {
        return Flyway.configure()
                .dataSource(rDataSource)
                .locations("classpath:db/migration/conta_r")
                .baselineOnMigrate(true)
                .load();
    }
    
}