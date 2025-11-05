package com.tom.first.library.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tom.first.library.model.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books_item")
public class BookItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "book_id")
	private Book book;
	
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "user_id")
	private User user;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name= "rent_start", nullable = true, unique = false, updatable = true)
	private LocalDate rentStart;
	
	@Column(name= "rent_end", nullable = true, unique = false, updatable = true)
	private LocalDate rentEnd;
	
}
