package com.tom.first.simple.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.mapper.EvaluationMapper;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.processes.events.EvaluationCreatedEvent;
import com.tom.first.simple.processes.events.EvaluationDeletedEvent;
import com.tom.first.simple.repository.EvaluationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EvaluationService {

	private final ApplicationEventPublisher eventPublisher;
	private final EvaluationRepository repository;
	private final EvaluationMapper mapper;
	private final UserService service;

	@Transactional(readOnly = true)
	public EvaluationResponse findById(long query) {
		return repository.findById(query).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("User with id '%s' was not found.", query));
		});
	}

	@Transactional(readOnly = true)
	public EvaluationResponse findBySubject(String subject) {
		return repository.findBySubject(subject).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("User with email '%s' was not found.", subject));
		});
	}

	public List<EvaluationResponse> findByGrade(double grade) {
		List<Evaluation> evaluations = repository.findByGrade(grade);
		if (evaluations.isEmpty()) {
			throw new RuntimeException(String.format("No evaluation found for grade: %.2f", grade));
		}
		return evaluations.stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional
	public EvaluationResponse createEvaluation(EvaluationRequest request) {
		if (repository.existsBySubject(request.subject())) {
			throw new RuntimeException(String.format("Evaluation with same name already exists %s", request.subject()));
		}
		var evaluation = mapper.build(request);
		var user = service.getUserByUsername(request.name());
		evaluation.setUserId(user.id());
		repository.save(evaluation);
		eventPublisher.publishEvent(new EvaluationCreatedEvent(evaluation));
		return mapper.toResponse(evaluation);
	}

	@Transactional
	public void deleteEvaluationById(long query) {
		var user = repository.findById(query).orElseThrow(() -> new RuntimeException("Evaluation Id not Found"));

		eventPublisher.publishEvent(new EvaluationDeletedEvent(user.getSubject()));
		repository.deleteById(query);
	}

	@Transactional
	public void deleteEvaluationBySubject(String subject) {
		if (!repository.existsBySubject(subject)) {
			throw new RuntimeException(String.format("No evaluation was found with the provided name: %s", subject));
		}
		eventPublisher.publishEvent(new EvaluationDeletedEvent(subject));
		repository.deleteBySubject(subject);
	}

}
