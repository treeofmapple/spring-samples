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
import com.tom.benchmark.order.exception.sql.DataViolationException;
import com.tom.benchmark.order.exception.sql.NotFoundException;
import com.tom.benchmark.order.logic.external.ClientService;
import com.tom.benchmark.order.mapper.OrderMapper;
import com.tom.benchmark.order.model.Order;
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

	private final OrderRepository repository;
	private final OrderMapper mapper;
	private final ClientService clientService;
	private final R2dbcEntityTemplate entityTemplate;

	// make it work even if the findByCpf don't work when the service gets off
	
	@Transactional(readOnly = true)
	public Mono<OrderResponse> searchOrderById(UUID orderId) {
		return repository.findById(orderId).map(mapper::toResponse)
				.switchIfEmpty(Mono.error(new NotFoundException("Order with ID: " + orderId + " was not found.")));
	}

	@Transactional(readOnly = true)
	public Mono<PageOrderResponse> searchClientByParams(int page) {
		Order probe = Order.builder().build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return repository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.collectList().map(list -> mapper.toResponse(list, page, PAGE_SIZE));
	}

	@Transactional
	public Mono<OrderResponse> createOrder(OrderRequest request) {
		return clientService.findByCpf(request.clientCpf())
				.switchIfEmpty(Mono.error(new NotFoundException("No client found with cpf provided")))
				.flatMap(client -> repository.existsByClientId(client.id()).flatMap(exists -> {
					if (exists) {
						return Mono.error(new DataViolationException("Client already has an existing order"));
					}

					Order order = mapper.build(request);
					order.setClientId(client.id());

					return repository.save(order).map(savedOrder -> mapper.toResponse(savedOrder, client.name()));
				}));
	}

	@Transactional
	public Mono<OrderResponse> updateOrder(OrderUpdate request) {
		return repository.findById(request.orderId())
				.switchIfEmpty(
						Mono.error(new NotFoundException("Order with ID: " + request.orderId() + " was not found.")))
				.flatMap(existentOrder -> {
					mapper.update(existentOrder, request);
					existentOrder.setId(request.orderId());
					return entityTemplate.update(existentOrder);
				}).map(mapper::toResponse);
	}

	@Transactional
	public Mono<Void> deleteOrder(UUID orderId) {
		return repository.existsById(orderId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return repository.deleteById(orderId);
			}
			return Mono.error(new NotFoundException("Client order with ID: " + orderId + " was not found."));
		});
	}

}
