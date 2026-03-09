package com.tom.benchmark.monolith.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.monolith.client.ClientRepository;
import com.tom.benchmark.monolith.order_items.OrderItem;
import com.tom.benchmark.monolith.order_items.OrderItemMapper;
import com.tom.benchmark.monolith.order_items.OrderItemRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderMapper mapper;
	private final OrderItemMapper itemMapper;

	private final OrderRepository orderRepository;
	private final ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public List<OrderResponse> findAll() {
		List<Order> orders = orderRepository.findAll();

		if (orders.isEmpty()) {
			return Collections.emptyList();
		}

		return orders.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> findByClientCpf(String cpf) {
		var client = clientRepository.findByCpf(cpf).orElseThrow();
		if (client == null) {
			throw new RuntimeException("No client found with cpf");
		}

		List<Order> orders = orderRepository.findByClientId(client.getId());
		if (orders.isEmpty()) {
			return new ArrayList<>();
		}

		return orders.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse addItemToOrder(Long orderId, OrderItemRequest request) {
		var order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Cannot add item. Order not found with ID: " + orderId));

		OrderItem newOrderItem = itemMapper.toOrderItem(request);
		newOrderItem.setOrder(order);

		order.getItems().add(newOrderItem);
		Order updatedOrder = orderRepository.save(order);

		return mapper.toResponse(updatedOrder);

	}

	@Transactional
	public OrderResponse createOrder(OrderRequest request) {
		if (!clientRepository.existsByCpf(request.clientCpf())) {
			throw new RuntimeException("No client found with cpf: " + request.clientCpf());
		}

		Order order = mapper.toOrder(request);
		List<OrderItem> items = request.items().stream().map(itemReq -> {
			OrderItem orderItem = itemMapper.toOrderItem(itemReq);
			orderItem.setOrder(order);
			return orderItem;
		}).collect(Collectors.toList());

		order.setItems(items);
		Order savedOrder = orderRepository.save(order);

		return mapper.toResponse(savedOrder);

	}

	@Transactional
	public void deleteOrderByClientCpf(String cpf) {
		var client = clientRepository.findByCpf(cpf)
				.orElseThrow(() -> new RuntimeException("No client found with cpf: " + cpf));

		List<Order> ordersToDelete = orderRepository.findByClientId(client.getId());

		if (!ordersToDelete.isEmpty()) {
			orderRepository.deleteAllInBatch(ordersToDelete);
		}

	}

}
