package com.tom.samples.ai.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.samples.ai.component.PromptComponent;
import com.tom.samples.ai.dto.PagePromptResponse;
import com.tom.samples.ai.dto.PromptRequest;
import com.tom.samples.ai.dto.PromptResponse;
import com.tom.samples.ai.dto.PromptUpdate;
import com.tom.samples.ai.mapper.PromptMapper;
import com.tom.samples.ai.model.Prompt;
import com.tom.samples.ai.repository.PromptRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class PromptService {

	@Value("${application.size.page:20}")
	private int PAGE_SIZE;
	
	@Value("${spring.ai.ollama.chat.options.model}")
	private String AI_MODEL;
	
	private final PromptRepository repository;
	private final ChatClient chatClient;
    private final PromptMapper mapper;
    private final EmbeddingModel embeddingModel;
    private final PromptComponent component;
    
	@Transactional(readOnly = true)
	public PromptResponse searchPromptById(UUID query) {
		var prompt = component.findById(query);
		return mapper.toResponse(prompt);
	}
    
	@Transactional(readOnly = true)
	public PagePromptResponse searchPromptByParams(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Prompt> prompt = repository.findAll(pageable);
		return mapper.toResponse(prompt);
	}
    
	@Transactional
	public Flux<String> createPrompt(PromptRequest request) {
		var prompt = mapper.build(request);
		StringBuilder fullResponse = new StringBuilder();
		
		Flux<String> aiContent = chatClient.prompt()
	            .user(request.prompt())
	            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, prompt.getId().toString()))
	            .stream()
	            .content()
	            .doOnNext(fullResponse::append)
	            .doOnComplete(() -> {
					String aiResponse = fullResponse.toString();
					float[] vector = embeddingModel.embed(request.prompt());
					prompt.setName(aiResponse.substring(0, 20).strip());
					prompt.setAiModel(AI_MODEL);
					prompt.setGeneratedPrompt(aiResponse);
					prompt.getMetadata().put("tokens", aiResponse.length() / 4);
					List<Double> embedding = IntStream.range(0, vector.length).mapToDouble(i -> vector[i]).boxed()
							.toList();
					prompt.setEmbedding(embedding);
					repository.save(prompt);
				});
		
		return Flux.concat(
		        Mono.just("[ID]:" + prompt.getId().toString()),
		        aiContent
		    );
	}
	
	@Transactional
	public Flux<String> continuePrompt(PromptUpdate request) {
		var prompt = component.findById(request.promptId());
		StringBuilder newSegmentBuilder = new StringBuilder();
	    Flux<String> aiContent = chatClient.prompt()
	            .user(request.prompt())
	            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.promptId().toString()))
	            .stream()
	            .content()
	            .doOnNext(newSegmentBuilder::append)
	            .doOnComplete(() -> {
	                prompt.setGeneratedPrompt(prompt.getGeneratedPrompt() + "\n\n" + newSegmentBuilder.toString());
	                repository.save(prompt);
	            });
	    
		return Flux.concat(
		        Mono.just("[ID]:" + prompt.getId().toString()),
		        aiContent
		    );
	}
	
	@Transactional
	public void deletePrompt(UUID query) {
		var prompt = component.findById(query);
		repository.delete(prompt);
	}
}
