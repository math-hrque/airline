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
    basePackages = "br.com.reserva.reserva.repositories.conta_cud",
    entityManagerFactoryRef = "cudEntityManagerFactory",
    transactionManagerRef = "cudTransactionManager"
)
public class ContaCUDEntityManagerConfigurations {

    @Bean(name = "cudEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean cudEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("cudDataSource") DataSource cudDataSource) {
        return builder
            .dataSource(cudDataSource)
            .packages("br.com.reserva.reserva.models.conta_cud")
            .persistenceUnit("conta_cud")
            .build();
    }

    @Bean(name = "cudTransactionManager")
    public PlatformTransactionManager cudTransactionManager(
            @Qualifier("cudEntityManagerFactory") EntityManagerFactory cudEntityManagerFactory) {
        return new JpaTransactionManager(cudEntityManagerFactory);
    }
    
}
