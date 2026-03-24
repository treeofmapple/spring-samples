package com.tom.front.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SystemConfiguration {

	@Bean
	public ForwardedHeaderFilter forwardedHeaderFilter() {
	    return new ForwardedHeaderFilter();
	}
	
}
