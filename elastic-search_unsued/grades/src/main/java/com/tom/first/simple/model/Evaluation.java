package com.tom.first.simple.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluation")
public class Evaluation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "subject", 
			nullable = false, 
			unique = false, 
			updatable = true)
	private String subject;

	@Column(name = "description", 
			nullable = true, 
			unique = false, 
			updatable = true)
	private String description;

	@Min(value = 0, message = "Can't be lower than 0")
	@Max(value = 10, message = "Can't be lower than 10")
	@Column(name = "grade", 
			nullable = false, 
			unique = false, 
			updatable = true)
	private Double grade;

	@Column(name = "user_id", 
			nullable = false)
	private Long userId;
	
}
