package com.tom.first.vehicle.model;

import com.tom.first.vehicle.model.enums.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle", indexes = {
	@Index(name = "idx_vehicle_plate", columnList = "plate")
	
})
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "brand_id", 
				nullable = false)
	private Brand brand;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "model_id",
				nullable = false)
	private Model model;
	
	@Column(name = "color",
			nullable = true,
			unique = false)
	private String color;
	
	@Column(name = "plate",
			nullable = false,
			unique = true)
	private String plate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type",
			nullable = false)
	private Type type;
	
}
