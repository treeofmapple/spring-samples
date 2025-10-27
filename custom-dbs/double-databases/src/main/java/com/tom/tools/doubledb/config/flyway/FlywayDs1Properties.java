package com.tom.tools.doubledb.config.flyway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Component
@Validated
@Getter
@Setter
@ConfigurationProperties(prefix = "application.flyway.ds1")
public class FlywayDs1Properties {

	@NotEmpty
	private String location;

}