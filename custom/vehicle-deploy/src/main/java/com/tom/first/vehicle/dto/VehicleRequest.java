package com.tom.first.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VehicleRequest (

	@NotBlank(message = "License plate cannot be blank.")
	@Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$", 
	         message = "The license plate must be in a valid format, such as AAA-1234 or AAA1A23.")
	String licensePlate,
		
    @NotBlank(message = "Brand cannot be blank.")
    String brand,

    @NotBlank(message = "Model cannot be blank.")
    String model,

    String color,

    @NotBlank(message = "Type cannot be blank.")
    String type

) {


}