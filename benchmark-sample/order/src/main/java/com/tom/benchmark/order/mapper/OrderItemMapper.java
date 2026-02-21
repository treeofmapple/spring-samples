package com.tom.benchmark.order.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.tom.benchmark.order.dto.OrderItemRequest;
import com.tom.benchmark.order.dto.OrderItemResponse;
import com.tom.benchmark.order.model.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

	OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "priceAtPurchase", ignore = true) 
	OrderItem toOrderItem(OrderItemRequest request);

    @Mapping(target = "productName", ignore = true)
	@Mapping(target = "itemTotal", expression = "java(calculateItemTotal(orderItem))")
	OrderItemResponse toResponse(OrderItem orderItem);
	
    default BigDecimal calculateItemTotal(OrderItem orderItem) {
        if (orderItem == null || orderItem.getPriceAtPurchase() == null) {
            return BigDecimal.ZERO;
        }
        return orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity()));
    }

}
