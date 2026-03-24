package com.tom.vehicle.normal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    String plate

) {

}
