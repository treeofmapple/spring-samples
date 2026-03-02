package com.tom.benchmark.order.repository;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tom.benchmark.order.model.Order;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID>, ReactiveQueryByExampleExecutor<Order> {

}
