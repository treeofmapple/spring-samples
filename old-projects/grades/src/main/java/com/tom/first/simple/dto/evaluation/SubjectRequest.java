package com.tom.first.simple.dto.evaluation;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequest(
		
		@NotBlank(message = "Subject cannot be blank")
		String subject
		
		) {

}
