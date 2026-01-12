package com.tom.aws.awstest.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
		
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        @Min(1)
        @Max(10)
        int quantity,

        @Positive(message = "O preço deve ser maior que zero")
        @DecimalMin(value = "1.00", message = "Preço mínimo é 1.00")
        BigDecimal price,
        
        String manufacturer
){

}
