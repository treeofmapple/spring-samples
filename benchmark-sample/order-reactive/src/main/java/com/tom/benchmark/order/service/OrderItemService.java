package com.tom.benchmark.order.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.order.dto.orderitem.OrderItemRequest;
import com.tom.benchmark.order.dto.orderitem.OrderItemResponse;
import com.tom.benchmark.order.exception.server.ServiceUnavailableException;
import com.tom.benchmark.order.exception.sql.DataViolationException;
import com.tom.benchmark.order.exception.sql.NotFoundException;
import com.tom.benchmark.order.logic.external.ProductService;
import com.tom.benchmark.order.mapper.OrderItemMapper;
import com.tom.benchmark.order.model.OrderItem;
import com.tom.benchmark.order.repository.OrderItemRepository;
import com.tom.benchmark.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderItemService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final OrderRepository orderRepository;
	private final OrderItemRepository itemRepository;
	private final OrderItemMapper mapper;
	private final ProductService productService;
	private final R2dbcEntityTemplate entityTemplate;

	@Transactional
	public Mono<OrderItemResponse> addItemToOrder(UUID orderId, OrderItemRequest request) {
		return productService.findBySku(request.productSku())
				.onErrorMap(e -> new ServiceUnavailableException("Service wasn't able to fetch data", e.getCause()))
				.switchIfEmpty(
						Mono.error(() -> new NotFoundException("Product not found with SKU: " + request.productSku())))
				.flatMap(product -> orderRepository.findById(orderId)
							.switchIfEmpty(Mono.error(() -> new NotFoundException("Order not found: " + orderId)))
							.flatMap(order -> {
								OrderItem orderItem = OrderItem.builder()
								.Id(UUID.randomUUID())
								.orderId(orderId)
								.productId(product.id())
								.quantity(request.quantity())
								.priceAtPurchase(product.price())
								.build();

								return entityTemplate.insert(orderItem)
										.map(data -> mapper.toResponse(data, product.name()));
							})
				);
	}

	@Transactional
	public Mono<Void> removeItemFromOrder(UUID orderId, UUID orderItemId) {
		return itemRepository.findById(orderItemId)
				.onErrorResume(e -> {
					return Mono.error(new ServiceUnavailableException("Service wasn't able to fetch data", e.getCause()));
				})
				.switchIfEmpty(Mono.error(() -> new NotFoundException("Order item not found: " + orderId)))
				.flatMap(item -> {
					if (!item.getOrderId().equals(orderId)) {
						return Mono.error(new DataViolationException("Item does not belong to current order"));
					}
					return itemRepository.delete(item);
				});
	}

}
