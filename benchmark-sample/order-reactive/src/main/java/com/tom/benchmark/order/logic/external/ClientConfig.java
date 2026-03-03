package com.tom.benchmark.order.logic.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

	@Bean
	ClientService clientService(@Value("${client.service-url}") String url) {
		WebClient client = WebClient.builder().baseUrl(url).build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
		return factory.createClient(ClientService.class);
	}

}
