package com.tom.benchmark.order.service;

import java.util.HashSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.order.dto.order.OrderRequest;
import com.tom.benchmark.order.dto.order.OrderResponse;
import com.tom.benchmark.order.dto.order.OrderUpdate;
import com.tom.benchmark.order.dto.order.PageOrderResponse;
import com.tom.benchmark.order.exception.server.ServiceUnavailableException;
import com.tom.benchmark.order.exception.sql.DataViolationException;
import com.tom.benchmark.order.exception.sql.NotFoundException;
import com.tom.benchmark.order.logic.external.ClientService;
import com.tom.benchmark.order.mapper.OrderMapper;
import com.tom.benchmark.order.model.Order;
import com.tom.benchmark.order.repository.OrderItemRepository;
import com.tom.benchmark.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final OrderRepository orderRepository;
	private final OrderItemRepository itemRepository;
	private final OrderMapper mapper;
	private final ClientService clientService;
	private final R2dbcEntityTemplate entityTemplate;

	@Transactional(readOnly = true)
	public Mono<OrderResponse> searchOrderById(UUID orderId) {
		return orderRepository.findById(orderId)
	            .switchIfEmpty(Mono.error(() -> new NotFoundException("Order with ID: " + orderId + " was not found.")))
	            .flatMap(order -> itemRepository.findAllByOrderId(order.getId())
	                    .collectList()
	                    .map(items -> {
	                        order.setItems(new HashSet<>(items));
	                        return order;
	                    }))
	            .flatMap(order -> clientService.findById(order.getClientId())
	                    .map(client -> mapper.toResponse(order, client.name(), client.cpf()))
	                    .onErrorResume(e -> Mono.just(mapper.toResponse(order, null, null))));
	}

	@Transactional(readOnly = true)
	public Mono<PageOrderResponse> searchOrderByParams(int page) {
		Order probe = Order.builder().build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return orderRepository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.filter(order -> order.getClientId() != null)
				.flatMap(order -> itemRepository.findAllByOrderId(order.getId())
	                    .collectList()
	                    .flatMap(items -> {
	                        order.setItems(new HashSet<>(items));
	                        return clientService.findById(order.getClientId())
	                                .map(client -> mapper.toResponse(order, client.name(), client.cpf()))
	                                .onErrorResume(e -> Mono.just(mapper.toResponse(order, null, null)));
	                    })
	            )
	            .collectList()
	            .map(content -> new PageOrderResponse(content, page, PAGE_SIZE, 0, (long) content.size()));
	}

	@Transactional // only creates with clientId
	public Mono<OrderResponse> createOrder(OrderRequest request) {
		return clientService.findByCpf(request.clientCpf())
				.onErrorMap(e -> new ServiceUnavailableException("Service wasn't able to fetch data", e))
				.switchIfEmpty(Mono.error(new NotFoundException("No client found with cpf provided")))
				.flatMap(client -> orderRepository.existsByClientId(client.id()).flatMap(exists -> {
					if (exists) {
						return Mono.<OrderResponse>error(
								new DataViolationException("Client already has an existing order"));
					}

					Order order = mapper.build(request);
					order.setClientId(client.id());
					return entityTemplate.insert(order)
							.map(savedOrder -> mapper.toResponse(savedOrder, client.name(), client.cpf()));
				}));
	}

	@Transactional // only updates with clientId but user can only have one order.
	public Mono<OrderResponse> updateOrder(OrderUpdate request) {
		return orderRepository.findById(request.orderId())
				.switchIfEmpty(
						Mono.error(new NotFoundException("Order with ID: " + request.orderId() + " was not found.")))
				.flatMap(existingOrder -> clientService.findByCpf(request.clientCpf())
						.switchIfEmpty(Mono
								.error(new NotFoundException("Client with CPF " + request.clientCpf() + " not found.")))
						.flatMap(client -> orderRepository.existsByClientId(client.id()).flatMap(hasOrder -> {
							if (hasOrder && !existingOrder.getClientId().equals(client.id())) {
								return Mono
										.error(new IllegalStateException("This client already has an active order."));
							}

							mapper.update(existingOrder, request);
							existingOrder.setClientId(client.id());
							existingOrder.setId(request.orderId());
							return entityTemplate.update(existingOrder);
						})))
				.flatMap(order -> clientService.findById(order.getClientId()).onErrorResume(e -> {
					return Mono
							.error(new ServiceUnavailableException("Service wasn't able to fetch data", e.getCause()));
				}).map(client -> mapper.toResponse(order, client.name(), client.cpf()))
						.onErrorResume(e -> Mono.just(mapper.toResponse(order, null, null))));
	}

	@Transactional // can delete without clientId
	public Mono<Void> deleteOrderById(UUID orderId) {
		return orderRepository.existsById(orderId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return orderRepository.deleteById(orderId);
			}
			return Mono.error(new NotFoundException("Client order with ID: " + orderId + " was not found."));
		});
	}

}
