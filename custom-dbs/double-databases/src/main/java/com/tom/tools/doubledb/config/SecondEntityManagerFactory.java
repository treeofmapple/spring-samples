package com.tom.tools.doubledb.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "ds2EntityManagerFactory", transactionManagerRef = "ds2TransactionManager", basePackages = "com.tom.tools.doubledb.ds2")
@EnableTransactionManagement
public class SecondEntityManagerFactory {

	@Bean
	LocalContainerEntityManagerFactoryBean ds2EntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("dataSourcePostgreSql") DataSource dataSource) {

		return builder.dataSource(dataSource).packages(packagesToScan()).persistenceUnit("ds2-pu")
				.properties(hibernateProperties()).build();
	}

	@Bean
	PlatformTransactionManager ds2TransactionManager(
			@Qualifier("ds2EntityManagerFactory") EntityManagerFactory secondEntityManagerFactory) {
		return new JpaTransactionManager(secondEntityManagerFactory);
	}

	protected String[] packagesToScan() {
		return new String[] { "com.tom.tools.doubledb.ds2" };
	}

	@SuppressWarnings("serial")
	protected Map<String, String> hibernateProperties() {
		return new HashMap<String, String>() {
			{
				put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				put("hibernate.jdbc.lob.non_contextual_creation", "true");
			}
		};
	}
}
