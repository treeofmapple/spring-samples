package com.tom.benchmark.monolith.product;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", indexes = {
		@Index(name = "idx_product_sku", columnList = "sku")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "sku", 
    		length = 50, 
    		nullable = false, 
    		unique = true)
    private String sku;
    
    @Column(name = "name", 
    		nullable = false, 
    		unique = false, 
    		updatable = true,
    		length = 100)
    private String name;
	
    @Column(name = "description", 
    		length = 2048, 
    		nullable = true, 
    		unique = false, 
    		updatable = true)
    private String description;

    @Column(name = "price", 
    		nullable = false, 
    		precision = 10, 
    		scale = 2, 
    		updatable = true)
    private BigDecimal price;
	
}
