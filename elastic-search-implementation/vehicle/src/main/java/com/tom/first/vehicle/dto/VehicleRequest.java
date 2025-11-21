package com.tom.first.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VehicleRequest (

    @NotNull(message = "Brand cannot be null.")
    @NotBlank(message = "Brand cannot be blank.")
    String brand,

    @NotNull(message = "Model cannot be null.")
    @NotBlank(message = "Model cannot be blank.")
    String model,

    @NotNull(message = "Color cannot be null.")
    @NotBlank(message = "Color cannot be blank.")
    String color,

    @NotNull(message = "License plate cannot be null.")
    @NotBlank(message = "License plate cannot be blank.")
    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$", 
             message = "The license plate must be in a valid format, such as AAA-1234 or AAA1A23.")
    String licensePlate,

    @NotNull(message = "Type cannot be null.")
    @NotBlank(message = "Type cannot be blank.")
    String type

) {
	
}