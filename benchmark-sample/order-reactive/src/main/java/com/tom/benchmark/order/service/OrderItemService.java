package com.tom.benchmark.order.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.order.dto.orderitem.OrderItemRequest;
import com.tom.benchmark.order.dto.orderitem.OrderItemResponse;
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
				.switchIfEmpty(
						Mono.error(() -> new NotFoundException("Product not found with SKU: " + request.productSku())))
				.flatMap(product -> {
					return orderRepository.findById(orderId)
							.switchIfEmpty(Mono.error(() -> new NotFoundException("Order not found: " + orderId)))
							.flatMap(order -> {
								OrderItem orderItem = new OrderItem();
								orderItem.setOrderId(orderId);
								orderItem.setProductId(product.id());
								orderItem.setQuantity(request.quantity());
								orderItem.setPriceAtPurchase(product.price());

								return entityTemplate.insert(orderItem).map(mapper::toResponse);
							});
				});
	}

	@Transactional
	public Mono<Void> removeItemFromOrder(UUID orderId, UUID orderItemId) {
		return itemRepository.findById(orderItemId)
				.switchIfEmpty(Mono.error(() -> new NotFoundException("Order item not found: " + orderId)))
				.flatMap(item -> {
					if (!item.getOrderId().equals(orderId)) {
						Mono.error(new DataViolationException("Item does not belong to current order"));
					}
					return itemRepository.delete(item);
				});
	}

}
