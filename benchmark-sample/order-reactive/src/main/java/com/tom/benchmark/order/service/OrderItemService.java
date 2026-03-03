package com.tom.benchmark.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;

import com.tom.benchmark.order.logic.external.ProductService;
import com.tom.benchmark.order.mapper.OrderItemMapper;
import com.tom.benchmark.order.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderItemService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final OrderItemRepository repository;
	private final OrderItemMapper mapper;
	private final ProductService productService;
	private final R2dbcEntityTemplate entityTemplate;

	
	// add and remove item to order
	
	
	
	
}
