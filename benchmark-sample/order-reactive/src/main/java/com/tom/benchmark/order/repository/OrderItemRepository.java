package com.tom.benchmark.order.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tom.benchmark.order.model.OrderItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepository
		extends ReactiveCrudRepository<OrderItem, UUID>, ReactiveQueryByExampleExecutor<OrderItem> {

	@Query(value = "SELECT * FROM order_item WHERE price_at_purchase = $1")
	Mono<OrderItem> findByPriceAtPurchase(BigDecimal priceAtPurchase);

	Flux<OrderItem> findAllByOrderId(UUID orderId);
	
	Mono<OrderItem> findByOrderIdAndProductId(UUID orderId, UUID productId);

}
