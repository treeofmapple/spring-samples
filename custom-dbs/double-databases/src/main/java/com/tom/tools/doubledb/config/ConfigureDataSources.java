package com.tom.tools.doubledb.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.tom.tools.doubledb.config.flyway.FlywayDs1Properties;
import com.tom.tools.doubledb.config.flyway.FlywayDs2Properties;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ConfigureDataSources {

	@Primary
	@Bean(name = "configMySql")
	@ConfigurationProperties("application.datasource.ds1")
	DataSourceProperties firstDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "dataSourceMySql")
	@ConfigurationProperties("application.datasource.ds1")
	HikariDataSource firstDataSource(@Qualifier("configMySql") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@FlywayDataSource
	@Bean(initMethod = "migrate")
	Flyway primaryFlyway(HikariDataSource primaryDataSource, FlywayDs1Properties properties) {
		return Flyway.configure().dataSource(primaryDataSource).locations(properties.getLocation()).load();
	}

	@Bean(name = "configPostgreSql")
	@ConfigurationProperties("application.datasource.ds2")
	DataSourceProperties secondDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "dataSourcePostgreSql")
	@ConfigurationProperties("application.datasource.ds2")
	HikariDataSource secondDataSource(@Qualifier("configPostgreSql") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@FlywayDataSource
	@Bean(initMethod = "migrate")
	Flyway secondFlyway(FlywayDs2Properties properties,
			@Qualifier("dataSourcePostgreSql") HikariDataSource secondDataSource) {
		return Flyway.configure().dataSource(secondDataSource).schemas(properties.getSchema())
				.locations(properties.getLocation()).load();
	}
}
