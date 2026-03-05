package com.tom.benchmark.order.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
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
@Table(name = "orders", schema = "shop")
public class Order {

	@Id
	@ToString.Include
	private UUID id;

	@ToString.Include
	@Column("client_id")
	private UUID clientId;

	@Builder.Default
	@Transient
    private Set<OrderItem> items = new HashSet<>();

	@ReadOnlyProperty
	@Column("created_at")
	private ZonedDateTime createdAt;

}
