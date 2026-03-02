package com.tom.benchmark.order.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.benchmark.order.dto.orderitem.OrderItemRequest;
import com.tom.benchmark.order.dto.orderitem.OrderItemResponse;
import com.tom.benchmark.order.dto.orderitem.OrderItemUpdate;
import com.tom.benchmark.order.dto.orderitem.PageOrderItemResponse;
import com.tom.benchmark.order.model.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

	@Mapping(target = "id", ignore = true)
	OrderItem build(OrderItemRequest request);

	OrderItemResponse toResponse(OrderItem orderItem);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget OrderItem orderItem, OrderItemUpdate update);

	default PageOrderItemResponse toResponse(List<OrderItem> list, Integer page, Integer size) {
		List<OrderItemResponse> content = list.stream().map(this::toResponse).toList();
		return new PageOrderItemResponse(content, page, size, 0, (long) list.size());
	}

}
