package com.tom.first.establishment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstablishmentRequest(

        @NotNull(message = "The name cannot be null.")
        @NotBlank(message = "The name cannot be blank.")
        String name, 

        @NotNull(message = "The CNPJ cannot be null.")
        @NotBlank(message = "The CNPJ cannot be blank.")
        String cnpj, 

        @NotNull(message = "The address cannot be null.")
        @NotBlank(message = "The address cannot be blank.")
        String address, 

        @NotNull(message = "The phone number cannot be null.")
        @NotBlank(message = "The phone number cannot be blank.")
        String phone,

        @NotNull(message = "The number of motorcycle spots cannot be null.")
        @Min(value = 1, message = "The number of motorcycle spots must be at least 1.")
        int motorcycleSpotCount, 

        @NotNull(message = "The number of car spots cannot be null.")
        @Min(value = 1, message = "The number of car spots must be at least 1.")
        int carSpotCount
) {
	
}