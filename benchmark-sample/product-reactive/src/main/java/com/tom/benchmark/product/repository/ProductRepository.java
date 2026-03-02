package com.tom.benchmark.product.repository;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tom.benchmark.product.model.Product;

import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository
		extends ReactiveCrudRepository<Product, UUID>, ReactiveQueryByExampleExecutor<Product> {

	Mono<Product> findBySku(String sku);

	Mono<Product> findByName(String name);

}
