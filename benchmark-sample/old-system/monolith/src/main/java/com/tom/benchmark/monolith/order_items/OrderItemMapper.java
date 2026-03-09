package com.tom.benchmark.monolith.order_items;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

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
