package com.tom.benchmark.order.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tom.benchmark.order.dto.ProductRequest;
import com.tom.benchmark.order.dto.ProductResponse;

import jakarta.validation.Valid;

@FeignClient(name = "product-service", url = "${feign.product-service.url}")
public interface ProductServiceFeign {

	@GetMapping(value = "/v1/product/sku", produces = MediaType.APPLICATION_JSON_VALUE)
	ProductResponse findBySku(@RequestParam String sku);
	
	@PostMapping(value = "/v1/product/create", 
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ProductResponse createProduct(@RequestBody @Valid ProductRequest request);
	
	@DeleteMapping(value = "/delete")
	Void deleteProduct(@RequestParam String sku);
}
