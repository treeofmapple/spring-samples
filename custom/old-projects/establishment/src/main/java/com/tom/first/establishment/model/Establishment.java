package com.tom.first.establishment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Establishment")
public class Establishment {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Column(unique = true)
	private String cnpj;

	private String address;

	private String telephone;

	private int vacanciesMotorcycles;

	private int vacanciesCars;
	
}
