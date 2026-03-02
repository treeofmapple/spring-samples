package com.tom.benchmark.order.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
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
@Table(name = "orders")
public class Order {

	@Id
	@ToString.Include
	private UUID id;

	@ToString.Include
	@Column("clientId")
	private UUID clientId;

	@Column("items")
	@MappedCollection(idColumn = "orders_id")
	private List<OrderItem> items;

	@ReadOnlyProperty
	@Column("created_at")
	private ZonedDateTime createdAt;

}
