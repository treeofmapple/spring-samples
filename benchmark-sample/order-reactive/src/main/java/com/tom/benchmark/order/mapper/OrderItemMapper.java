package com.tom.benchmark.order.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.benchmark.order.dto.orderitem.OrderItemResponse;
import com.tom.benchmark.order.dto.orderitem.OrderItemUpdate;
import com.tom.benchmark.order.model.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {
		java.math.BigDecimal.class })
public interface OrderItemMapper {

	@Mapping(target = "itemTotal", expression = "java(orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())))")
	OrderItemResponse toResponse(OrderItem orderItem, String productName);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget OrderItem orderItem, OrderItemUpdate update);

}
