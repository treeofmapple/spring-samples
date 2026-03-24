package com.tom.vehicle.normal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "brand",
			nullable = false)
	private String brand;

	@Column(name = "model",
			nullable = false)
	private String model;

	@Column(name = "color",
			nullable = false)
	private String color;

	@Column(name = "plate",
			nullable = false,
			unique = true)
	private String plate;

}
