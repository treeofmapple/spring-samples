package com.tom.samples.ai.dto;

import java.util.List;

public record PagePromptResponse(
		
		List<PromptResponse> content,
		Integer page,
		Integer size,
		Integer totalPages
		
		) {

}
