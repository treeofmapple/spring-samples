package com.tom.benchmark.product.model;

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
@Table(name = "product")
public class Product {

	@Id
	@ToString.Include
	private UUID id;
	
	@ToString.Include
	@Column("sku")
	private String sku;
	
	@ToString.Include
	@Column("name")
	private String name;
	
	@ToString.Include
	@Column("description")
	private String description;
	
	@ToString.Include
	@Column("price")
	private BigDecimal price;
	
	@ReadOnlyProperty
	@Column("created_at")
	private ZonedDateTime createdAt;
	
}
