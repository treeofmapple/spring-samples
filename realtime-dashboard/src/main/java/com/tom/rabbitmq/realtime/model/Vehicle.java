package com.tom.rabbitmq.realtime.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle")
public class Vehicle extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "plate_number",
			nullable = false)
	private String plateNumber;

	@Column(name = "model",
			nullable = false)
	private String model;

	private Double latitude;
	private Double longitude;
	private Double speedKmH;

	@Min(0)
	@Max(100)
	private Integer fuelPercent;
	private Double engineTemperature;
	private Double odometerKm;

	/*
	 * @JsonManagedReference
	 *
	 * @OneToMany(mappedBy = "vehicle") private List<Delivery> delivery;
	 */
}
