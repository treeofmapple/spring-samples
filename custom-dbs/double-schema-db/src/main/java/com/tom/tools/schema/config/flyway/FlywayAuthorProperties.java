package com.tom.tools.schema.config.flyway;

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
@ConfigurationProperties(prefix = "application.flyway.ds2")
public class FlywayAuthorProperties {

	@NotEmpty
	private String location;

	@NotEmpty
	private String schema;

}
