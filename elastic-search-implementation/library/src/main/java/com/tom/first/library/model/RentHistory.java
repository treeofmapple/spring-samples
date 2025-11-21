package com.tom.first.library.model;

import java.time.LocalDate;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rent_history")
public class RentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", 
			nullable = false)
    private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", 
			nullable = false)
	private Status status;
    
	@Column(name = "rent_start", 
			nullable = true, 
			unique = false, 
			updatable = true)
	private LocalDate rentStart;
	
	@Column(name = "rent_end", 
			nullable = true, 
			unique = false, 
			updatable = true)
	private LocalDate rentEnd;
	
    @ManyToOne
    @JoinColumn(name = "book_item_id")
    private BookItem bookItem;
    
}
