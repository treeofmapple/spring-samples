package com.tom.samples.ai.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.samples.ai.dto.PagePromptResponse;
import com.tom.samples.ai.dto.PromptRequest;
import com.tom.samples.ai.dto.PromptResponse;
import com.tom.samples.ai.dto.PromptUpdate;
import com.tom.samples.ai.model.Prompt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PromptMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "userPrompt", source = "prompt")
    @Mapping(target = "metadata", expression = "java(new java.util.HashMap<>())")
	Prompt build(PromptRequest request);

	PromptResponse toResponse(Prompt prompt);

	@Mapping(target = "id", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Prompt prompt, PromptUpdate request);

	List<PromptResponse> toResponseList(List<Prompt> prompt);

	default PagePromptResponse toResponse(Page<Prompt> page) {
		if (page == null) {
			return null;
		}
		List<PromptResponse> content = toResponseList(page.getContent());
		return new PagePromptResponse(content, page.getNumber(), page.getSize(), page.getTotalPages());
	}

}
