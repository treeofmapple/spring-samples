package com.tom.benchmark.order.logic.external;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.tom.benchmark.order.logic.product.ProductResponse;

import reactor.core.publisher.Mono;

public interface ProductService {

	@GetExchange("/v1/product/{id}")
	Mono<ProductResponse> findById(@PathVariable(value = "id") UUID productId);

	@GetExchange("/v1/product/{sku}")
	Mono<ProductResponse> findBySku(@PathVariable(value = "sku") String productSku);
	
}
