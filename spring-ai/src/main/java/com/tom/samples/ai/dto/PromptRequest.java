package com.tom.samples.ai.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(

		@JsonAlias( {
				"prompt", "message" })
		@NotBlank(message = "The user Prompt can't be null")
		String prompt

		) {

}
