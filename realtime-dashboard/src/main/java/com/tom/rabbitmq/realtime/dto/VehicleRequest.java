package com.tom.rabbitmq.realtime.dto;

import jakarta.validation.constraints.NotBlank;

public record VehicleRequest(

		@NotBlank(message = "Plate number can't be empty!")
	    String plateNumber,
	    
	    @NotBlank(message = "Car model can't be empty!")
	    String model

	) {

}
