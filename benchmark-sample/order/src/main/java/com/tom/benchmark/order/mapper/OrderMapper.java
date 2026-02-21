package com.tom.benchmark.order.mapper;

import java.math.BigDecimal;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.tom.benchmark.order.dto.OrderRequest;
import com.tom.benchmark.order.dto.OrderResponse;
import com.tom.benchmark.order.model.Order;

@Mapper(componentModel = "spring", 
uses = {OrderItemMapper.class},
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "items", ignore = true)
	Order toOrder(OrderRequest request);

    @Mapping(target = "clientName", ignore = true)
    @Mapping(source = "items", target = "items")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(order, orderItemMapper))")
    OrderResponse toResponse(Order order, @Context OrderItemMapper orderItemMapper);

    default BigDecimal calculateTotalPrice(Order order, @Context OrderItemMapper orderItemMapper) {
        if (order == null || order.getItems() == null || orderItemMapper == null) {
            return BigDecimal.ZERO;
        }
        return order.getItems().stream()
                .map(orderItemMapper::toResponse)
                .map(itemResponse -> itemResponse.itemTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
}
