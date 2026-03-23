package com.tom.samples.ai.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public record PromptUpdate(
		
		@JsonAlias( {
				"promptId", "id" })
		@NotBlank(message = "The prompt Id can't be empty")
		UUID promptId,
		
		String prompt
		
		) {

}
