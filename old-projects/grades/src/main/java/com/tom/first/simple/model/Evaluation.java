package com.tom.first.simple.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "evaluation")
public class Evaluation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "subject", nullable = false, unique = false, updatable = true)
	private String subject;

	@Column(name = "description", nullable = true, unique = false, updatable = true)
	private String description;

	@Column(name = "grade", nullable = false, unique = false, updatable = true)
	private double grade;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
}
