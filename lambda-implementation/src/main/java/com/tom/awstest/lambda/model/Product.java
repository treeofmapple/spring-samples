package com.tom.awstest.lambda.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product", indexes = {
		@Index(name = "idx_product_name", columnList = "name")
})
public class Product extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", 
			nullable = false, 
			unique = true)
	private String name;

	@Column(name = "quantity", 
			nullable = true, 
			unique = false)
	private Integer quantity;

	@Column(name = "price", 
			nullable = true, 
			unique = false,
			precision = 10,
			scale = 2)
	private BigDecimal price;

	@Column(name = "manufacturer", 
			nullable = true, 
			unique = false)
	private String manufacturer;

	@Column(name = "active", 
			nullable = false, 
			unique = false)
	private Boolean active;

}
