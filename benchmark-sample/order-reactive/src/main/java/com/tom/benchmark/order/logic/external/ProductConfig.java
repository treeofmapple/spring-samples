package com.tom.benchmark.order.logic.external;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import reactor.core.publisher.Mono;

@Configuration
public class ProductConfig {

	@Bean
	ProductService productService(
		WebClient.Builder loadBalancedBuilder
		/*	@Value("${service.product.url}") String url */
		) {
		WebClient client = loadBalancedBuilder.baseUrl("http://product-benchmark")
				.defaultStatusHandler(status -> status == HttpStatus.NOT_FOUND, response -> Mono.empty()).build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
		return factory.createClient(ProductService.class);
	}

}
