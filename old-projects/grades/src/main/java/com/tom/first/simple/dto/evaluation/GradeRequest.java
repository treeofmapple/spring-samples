package com.tom.first.simple.dto.evaluation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GradeRequest(
        
		@Min(value = 0, message = "The grade must be at least 0.")
        @Max(value = 10, message = "The grade must be at most 10.")
        double grade
		) {

}
