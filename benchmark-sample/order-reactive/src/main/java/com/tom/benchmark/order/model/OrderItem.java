package com.tom.benchmark.order.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id
    @ToString.Include
    private UUID Id;

    @Column("orders_id") // , nullable = false)
    private UUID orderId;

    @Column("product_id") //, nullable = false)
    private UUID productId;

    @ToString.Include
    @Column("quantity") // , nullable = false)
    private Integer quantity;

    @ToString.Include
    @Column("price_at_purchase") //, nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;

    @ReadOnlyProperty
	@Column("created_at")
	private ZonedDateTime createdAt;

}
