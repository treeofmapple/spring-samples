package com.tom.benchmark.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.benchmark.order.dto.OrderItemRequest;
import com.tom.benchmark.order.dto.OrderRequest;
import com.tom.benchmark.order.dto.OrderResponse;
import com.tom.benchmark.order.mapper.OrderItemMapper;
import com.tom.benchmark.order.mapper.OrderMapper;
import com.tom.benchmark.order.model.Order;
import com.tom.benchmark.order.model.OrderItem;
import com.tom.benchmark.order.openfeign.ClientServiceFeign;
import com.tom.benchmark.order.openfeign.ProductServiceFeign;
import com.tom.benchmark.order.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repository;
	private final OrderMapper mapper;
	private final OrderItemMapper itemMapper;
	private final ClientServiceFeign clientFeign;
	private final ProductServiceFeign productFeign;
	
	
	public List<OrderResponse> findAll() {
		List<Order> orders = repository.findAll();
		if (orders.isEmpty()) {
			throw new RuntimeException("No orders found in the database.");
		}
		return orders.stream().map(order -> mapper.toResponse(order, itemMapper)).collect(Collectors.toList());
	}

	public OrderResponse findById(Long id) {
        return repository.findById(id).map(order -> mapper.toResponse(order, itemMapper))
                .orElseThrow(() -> 
                	new RuntimeException("Order not found with ID: " + id)
                );
	}

	public List<OrderResponse> findByClientCpf(String cpf) {
		var client = clientFeign.findByCpf(cpf);
		if(client == null) {
			throw new RuntimeException("No client found with cpf");
		}
		
		List<Order> orders = repository.findByClientId(client.id());
		if (orders.isEmpty()) {
			return new ArrayList<>();
		}
		return orders.stream()
				.map(order -> mapper.toResponse(order, itemMapper))
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse addItemToOrder(Long orderId, OrderItemRequest request) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Cannot add item. Order not found with ID: " + orderId));
        
        var product = productFeign.findBySku(request.productSku());
        if(product == null) {
        	throw new RuntimeException("No product found with sku");
        }
        
        var newOrderItem = itemMapper.toOrderItem(request);
        newOrderItem.setProductId(product.id());
        newOrderItem.setPriceAtPurchase(product.price());
        newOrderItem.setOrder(order);

        order.getItems().add(newOrderItem);
        Order updatedOrder = repository.save(order);

        return mapper.toResponse(updatedOrder, itemMapper);
		
	}

	@Transactional
	public OrderResponse createOrder(OrderRequest request) {
		var client = clientFeign.findByCpf(request.clientCpf());
		if(client == null) {
			throw new RuntimeException("No client found with cpf");
		}
		
	    var order = mapper.toOrder(request);
	    order.setClientId(client.id());

	    List<OrderItem> items = request.items().stream()
	            .map(itemReq -> {
	                var product = productFeign.findBySku(itemReq.productSku());;
	                if(product == null) {
	                	throw new RuntimeException("Product not found with SKU: " + itemReq.productSku());
	                }

	                OrderItem orderItem = itemMapper.toOrderItem(itemReq);
	                orderItem.setProductId(product.id());
	                orderItem.setPriceAtPurchase(product.price());
	                orderItem.setOrder(order);
	                return orderItem;
	            }).collect(Collectors.toList());
	    
        order.setItems(items);
        var savedOrder = repository.save(order);
        return mapper.toResponse(savedOrder, itemMapper);
		
	}

	@Transactional
	public void deleteOrderByClientCpf(String cpf) {
		var client = clientFeign.findByCpf(cpf);
		if(client == null) {
			throw new RuntimeException("No client found with cpf");
		}

		List<Order> ordersToDelete = repository.findByClientId(client.id());
		
        if (!ordersToDelete.isEmpty()) {
            repository.deleteAllInBatch(ordersToDelete);
        }
		
	}

}
