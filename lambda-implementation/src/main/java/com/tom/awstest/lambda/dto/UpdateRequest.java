package com.tom.awstest.lambda.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateRequest(
		
        String name,

        @Size(min = 1, max = 10, message = "O minimo é 1 o máximo é 10")
        int quantity,

        @Positive(message = "O preço deve ser maior que zero")
        @Size(min = 1, message = "O minimo é 1 o máximo é 10")
        BigDecimal price,
        
        String manufacturer
		
		) {

}
