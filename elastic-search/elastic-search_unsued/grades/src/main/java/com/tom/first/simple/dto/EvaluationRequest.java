package com.tom.first.simple.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EvaluationRequest(
		
        @NotBlank(message = "The subject cannot be blank.")
        String subject,

        @NotBlank(message = "The description cannot be blank.")
        String description,

        @Min(value = 0, message = "The grade must be at least 0.")
        @Max(value = 10, message = "The grade must be at most 10.")
        double grade,

        @NotBlank(message = "The evaluator's name cannot be blank.")
        String name
) {

}
