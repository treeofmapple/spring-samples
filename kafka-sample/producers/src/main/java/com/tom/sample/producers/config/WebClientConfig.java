package com.tom.sample.producers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig 
{

	@Bean
	WebClient.Builder webClientBuilder()
	{
		return WebClient.builder();
	}
	
}
