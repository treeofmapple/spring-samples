package com.tom.first.simple.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.first.simple.dto.EvaluationOutboxBuild;
import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.model.EvaluationDocument;
import com.tom.first.simple.model.EvaluationOutbox;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EvaluationMapper {

	@Mapping(target = "id", ignore = true)
	Evaluation build(EvaluationRequest request);
	
	@Mapping(target = "id", ignore = true)
	Evaluation build(EvaluationDocument doc);
	
	@Mapping(target = "id", ignore = true)
	EvaluationDocument build(Evaluation evaluation);
	
	EvaluationOutbox build(EvaluationOutboxBuild outboxBuild);
	
	EvaluationResponse toResponse(Evaluation evaluation);
	
}
