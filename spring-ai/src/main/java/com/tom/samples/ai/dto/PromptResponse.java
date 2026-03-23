package com.tom.samples.ai.dto;

import java.time.Instant;
import java.util.UUID;

public record PromptResponse(
		
		UUID id,
		String name,
		String aiModel,
		String userPrompt,
		String generatedPrompt,
		Instant createdAt,
		Instant updatedAt
		
		) {

}
