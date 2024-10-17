package br.com.reserva.reserva.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = "br.com.reserva.reserva.repositories.conta_r",
    entityManagerFactoryRef = "rEntityManagerFactory",
    transactionManagerRef = "rTransactionManager"
)
public class ContaREntityManagerConfigurations {

    @Bean(name = "rEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("rDataSource") DataSource rDataSource) {
        return builder
            .dataSource(rDataSource)
            .packages("br.com.reserva.reserva.models.conta_r")
            .persistenceUnit("conta_r")
            .build();
    }

    @Bean(name = "rTransactionManager")
    public PlatformTransactionManager rTransactionManager(
            @Qualifier("rEntityManagerFactory") EntityManagerFactory rEntityManagerFactory) {
        return new JpaTransactionManager(rEntityManagerFactory);
    }
    
}
