package com.tom.benchmark.product.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.product.dto.PageProductResponse;
import com.tom.benchmark.product.dto.ProductRequest;
import com.tom.benchmark.product.dto.ProductResponse;
import com.tom.benchmark.product.dto.ProductUpdate;
import com.tom.benchmark.product.exception.sql.DataViolationException;
import com.tom.benchmark.product.exception.sql.NotFoundException;
import com.tom.benchmark.product.mapper.ProductMapper;
import com.tom.benchmark.product.model.Product;
import com.tom.benchmark.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final ProductRepository repository;
	private final ProductMapper mapper;
	private final R2dbcEntityTemplate entityTemplate;
	
	@Transactional(readOnly = true)
	public Mono<ProductResponse> searchProductById(UUID productId) {
		return repository.findById(productId).map(mapper::toResponse)
				.switchIfEmpty(Mono.error(new NotFoundException("Product with ID: " + productId + " was not found.")));
	}

	@Transactional(readOnly = true)
	public Mono<PageProductResponse> searchProductByParams(int page, String sku, String name, BigDecimal price) {
		Product probe = Product.builder().sku(sku).name(name).price(price).build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return repository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.collectList().map(list -> mapper.toResponse(list, page, PAGE_SIZE));
	}

	@Transactional
	public Mono<ProductResponse> createProduct(ProductRequest request) {
		return repository.findBySku(request.sku()).flatMap(existentUser -> {
			return Mono.<Product>error(
					new DataViolationException("Product with sku: " + request.sku() + " already exists."));
		}).switchIfEmpty(Mono.defer(() -> {
			Product product = mapper.build(request);
			return entityTemplate.insert(product);
		})).map(mapper::toResponse);
	}

	@Transactional
	public Mono<ProductResponse> updateProduct(ProductUpdate request) {
		return repository.findById(request.productId())
				.switchIfEmpty(
						Mono.error(new NotFoundException("Product with ID: " + request.productId() + " was not found.")))
				.flatMap(existentUser -> {
					mapper.update(existentUser, request);
					existentUser.setId(request.productId());
					return entityTemplate.update(existentUser);
				}).map(mapper::toResponse);
	}

	@Transactional
	public Mono<Void> deleteProduct(UUID userId) {
		return repository.existsById(userId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return repository.deleteById(userId);
			}
			return Mono.error(new NotFoundException("Client with ID: " + userId + " was not found."));
		});
	}
	
}
