package com.tom.first.library.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "books")
public class Book extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name= "title", nullable = false, unique = true, updatable = true)
	private String title;
	
	@Column(name= "author", nullable = true, unique = false, updatable = true)
	private String author;
	
	@Column(name= "quantity", nullable = false, unique = false, updatable = true)
	private int quantity;
	
	@Column(name= "price", nullable = true, unique = false, updatable = true)
	private double price;
	
	@Column(name= "launch_year", nullable = true, unique = false, updatable = true)
	private LocalDate launchYear;
	
	@OneToMany(mappedBy = "book")
	private List<BookItem> bookItems;
	
}
