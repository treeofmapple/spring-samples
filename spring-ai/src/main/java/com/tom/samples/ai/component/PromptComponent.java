package com.tom.samples.ai.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tom.samples.ai.exception.sql.NotFoundException;
import com.tom.samples.ai.model.Prompt;
import com.tom.samples.ai.repository.PromptRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PromptComponent {

	private final PromptRepository repository;
	
	public Prompt findById(UUID promptId) {
		return repository.findById(promptId)
				.orElseThrow(() -> new NotFoundException("The Prompt ID: " + promptId + " was not found."));
	}
	
}
