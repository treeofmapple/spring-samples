package com.tom.samples.ai.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tom.samples.ai.global.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "prompt")
public class Prompt extends Auditable {

	@Id
	@ToString.Include
	private UUID id;
	
	@ToString.Include
	@Field(name = "name") 
	private String name;
	
	@ToString.Include
	@Field(name = "aiModel")
	private String aiModel;

	@Field(name = "user_prompt")
	private String userPrompt;
	
	@Field(name = "generated_prompt")
	private String generatedPrompt;
	
	@Field(name = "metadata")
    private Map<String, Object> metadata;
	
	@Field(name = "embedding")
    private List<Double> embedding;
	
}
