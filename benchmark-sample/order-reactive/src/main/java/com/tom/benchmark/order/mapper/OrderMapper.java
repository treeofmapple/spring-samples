package com.tom.benchmark.order.mapper;

import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.benchmark.order.dto.order.OrderRequest;
import com.tom.benchmark.order.dto.order.OrderResponse;
import com.tom.benchmark.order.dto.order.OrderUpdate;
import com.tom.benchmark.order.model.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { OrderItemMapper.class }, imports = { UUID.class })
public interface OrderMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "items", expression = "java(new java.util.HashSet<>())")
	Order build(OrderRequest request);
	
	OrderResponse toResponse(Order order, String clientName, String clientCpf);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Order order, OrderUpdate update);
	
}
