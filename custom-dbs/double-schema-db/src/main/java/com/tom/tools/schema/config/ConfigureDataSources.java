package com.tom.tools.schema.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.tom.tools.schema.config.flyway.FlywayAuthorProperties;
import com.tom.tools.schema.config.flyway.FlywayBookProperties;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class ConfigureDataSources {

	@Primary
	@Bean(name = "configBooksSchema")
	@ConfigurationProperties("application.datasource.ds1")
	DataSourceProperties firstDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "dataSourceBooksSchema")
	@ConfigurationProperties("application.datasource.ds1")
	HikariDataSource firstDataSource(@Qualifier("configBooksSchema") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@FlywayDataSource
	@Bean(initMethod = "migrate")
	Flyway firstFlyway(FlywayBookProperties properties,
			@Qualifier("dataSourceBooksSchema") HikariDataSource dataSource) {
		return Flyway.configure().dataSource(dataSource).schemas(properties.getSchema())
				.locations(properties.getLocation()).load();
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------

	@Bean(name = "configAuthorsSchema")
	@ConfigurationProperties("application.datasource.ds2")
	DataSourceProperties secondDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "dataSourceAuthorsSchema")
	@ConfigurationProperties("application.datasource.ds2")
	HikariDataSource secondDataSource(@Qualifier("configAuthorsSchema") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@FlywayDataSource
	@Bean(name = "flywayAuthorsSchema", initMethod = "migrate")
	Flyway secondFlyway(FlywayAuthorProperties properties,
			@Qualifier("dataSourceAuthorsSchema") HikariDataSource dataSource) {
		return Flyway.configure().dataSource(dataSource).schemas(properties.getSchema())
				.locations(properties.getLocation()).load();
	}
}
