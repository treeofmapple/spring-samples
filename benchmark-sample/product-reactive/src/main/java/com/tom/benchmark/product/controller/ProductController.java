package com.tom.benchmark.product.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.product.dto.PageProductResponse;
import com.tom.benchmark.product.dto.ProductRequest;
import com.tom.benchmark.product.dto.ProductResponse;
import com.tom.benchmark.product.dto.ProductUpdate;
import com.tom.benchmark.product.service.ProductService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductResponse> searchProductById(@PathVariable(value = "id") UUID userId) {
		return service.searchProductById(userId);
	}

	@GetMapping(value = "/search")
	@ResponseStatus(HttpStatus.OK)
	public Mono<PageProductResponse> searchProductByParams(
			@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
			@RequestParam(required = false) String sku, @RequestParam(required = false) String name,
			@RequestParam(required = false) BigDecimal price) {
		return service.searchProductByParams(page, sku, name, price);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductResponse> createProduct(@RequestBody ProductRequest request) {
		return service.createProduct(request);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<ProductResponse> updateProduct(@RequestBody ProductUpdate request) {
		return service.updateProduct(request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteProduct(@PathVariable(value = "id") UUID userId) {
		return service.deleteProduct(userId);
	}

}
