package com.tom.first.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
	@Value("${webflux.api.user}")
	private String userApi;
	
	@Bean
    WebClient userServiceClient() {
        return WebClient.builder()
                .baseUrl(userApi) 
                .build();
    }
}