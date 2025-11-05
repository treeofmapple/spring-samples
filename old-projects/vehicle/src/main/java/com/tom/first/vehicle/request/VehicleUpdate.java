package com.tom.first.vehicle.request;

import com.tom.first.vehicle.model.enums.Type;

import jakarta.validation.constraints.Pattern;

public record VehicleUpdate(

	    String brand,

	    String model,

	    String color,

	    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$", 
	             message = "The license plate must be in a valid format, such as AAA-1234 or AAA1A23.")
	    String licensePlate,

	    Type type

) {
}