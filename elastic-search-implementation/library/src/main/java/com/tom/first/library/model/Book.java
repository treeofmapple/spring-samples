package com.tom.first.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
@EqualsAndHashCode(callSuper = true)
public class Book extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name= "title", 
			nullable = false, 
			unique = true, 
			updatable = true)
	private String title;
	
	@Column(name= "author", 
			nullable = true, 
			unique = false, 
			updatable = true)
	private String author;
	
	@Min(1)
	@Max(1000)
	@Column(name= "quantity", 
			nullable = false, 
			unique = false, 
			updatable = true)
	private Integer quantity;
	
	@DecimalMin(value = "0.00")
	@Column(name= "price", 
			nullable = true, 
			unique = false, 
			updatable = true,
	        precision = 10,
	        scale = 2)
	private BigDecimal price;
	
	@Column(name= "launch_year", 
			nullable = true, 
			unique = false, 
			updatable = true)
	private LocalDate launchYear;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "book")
	private List<BookItem> bookItems;
	
}
