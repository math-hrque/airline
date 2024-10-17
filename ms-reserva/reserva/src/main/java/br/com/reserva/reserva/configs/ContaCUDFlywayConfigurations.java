package br.com.reserva.reserva.configs;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ContaCUDFlywayConfigurations {

    @Bean(name = "flywayCud")
    public Flyway flywayCud(@Qualifier("cudDataSource") DataSource cudDataSource) {
        return Flyway.configure()
                .dataSource(cudDataSource)
                .locations("classpath:db/migration/conta_cud")
                .baselineOnMigrate(true)
                .load();
    }
    
}