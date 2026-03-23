package com.tom.samples.ai.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.samples.ai.dto.PagePromptResponse;
import com.tom.samples.ai.dto.PromptRequest;
import com.tom.samples.ai.dto.PromptResponse;
import com.tom.samples.ai.dto.PromptUpdate;
import com.tom.samples.ai.service.PromptService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai/prompt")
public class PromptController {

	private final PromptService service;

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PromptResponse searchPromptById(@PathVariable(value = "id") UUID promptId) {
		return service.searchPromptById(promptId);
	}

	@GetMapping(value = "/search")
	@ResponseStatus(HttpStatus.OK)
	public PagePromptResponse searchPromptByParams(@RequestParam(required = false, defaultValue = "0") int page) {
		return service.searchPromptByParams(page);
	}

	@PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Flux<String> createPrompt(@RequestBody PromptRequest request) {
		return service.createPrompt(request);
	}

	@PutMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Flux<String> continuePrompt(@RequestBody PromptUpdate request) {
		return service.continuePrompt(request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePrompt(@PathVariable(value = "id") UUID promptId) {
		service.deletePrompt(promptId);
	}

}
