package com.tom.benchmark.order.service;

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
import com.tom.benchmark.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final OrderRepository repository;
	private final OrderMapper mapper;
	private final ClientService clientService;
	private final R2dbcEntityTemplate entityTemplate;

	@Transactional(readOnly = true)
	public Mono<OrderResponse> searchOrderById(UUID orderId) {
		return repository.findById(orderId)
				.switchIfEmpty(Mono.error(new NotFoundException("Order with ID: " + orderId + " was not found.")))
				.flatMap(order -> clientService.findById(order.getClientId())
						.map(client -> mapper.toResponse(order, client.name()))
						.onErrorResume(e -> Mono.just(mapper.toResponse(order, null))));
	}

	@Transactional(readOnly = true)
	public Mono<PageOrderResponse> searchOrderByParams(int page) {
		Order probe = Order.builder().build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return repository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.collectList()
				.flatMap(list -> Flux.fromIterable(list)
						.flatMap(order -> clientService.findById(order.getClientId())
								.map(client -> mapper.toResponse(order, client.name()))
								.onErrorResume(e -> Mono.just(mapper.toResponse(order, null))))
						.collectList()
						.map(content -> new PageOrderResponse(content, page, PAGE_SIZE, 0, (long) list.size())));
	}

	@Transactional // only creates with clientId
	public Mono<OrderResponse> createOrder(OrderRequest request) {
		return clientService.findByCpf(request.clientCpf())
				.onErrorResume(e -> {
					return Mono.error(new ServiceUnavailableException("Service wasn't able to fetch data", e.getCause()));
				})
				.switchIfEmpty(Mono.error(new NotFoundException("No client found with cpf provided")))
				.flatMap(client -> repository.existsByClientId(client.id()).flatMap(exists -> {
					if (exists) {
						return Mono.<OrderResponse>error(
								new DataViolationException("Client already has an existing order"));
					}

					Order order = mapper.build(request);
					order.setClientId(client.id());

					return entityTemplate.insert(order).map(savedOrder -> mapper.toResponse(savedOrder, client.name()));
				}));
	}

	@Transactional // only updates with clientId
	public Mono<OrderResponse> updateOrder(OrderUpdate request) {
		return repository.findById(request.orderId())
				.switchIfEmpty(
						Mono.error(new NotFoundException("Order with ID: " + request.orderId() + " was not found.")))
				.flatMap(existentOrder -> {
					mapper.update(existentOrder, request);
					existentOrder.setId(request.orderId());
					return entityTemplate.update(existentOrder);
				})
				.flatMap(order -> clientService.findById(order.getClientId())
						.onErrorResume(e -> {
							return Mono.error(new ServiceUnavailableException("Service wasn't able to fetch data", e.getCause()));
						})
						.map(client -> mapper.toResponse(order, client.name()))
						.onErrorResume(e -> Mono.just(mapper.toResponse(order, null))));
	}

	@Transactional // can delete without clientId
	public Mono<Void> deleteOrderById(UUID orderId) {
		return repository.existsById(orderId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return repository.deleteById(orderId);
			}
			return Mono.error(new NotFoundException("Client order with ID: " + orderId + " was not found."));
		});
	}

}
