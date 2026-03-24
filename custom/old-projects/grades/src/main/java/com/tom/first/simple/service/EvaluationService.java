package com.tom.first.simple.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.dto.evaluation.EvaluationUpdate;
import com.tom.first.simple.dto.evaluation.GradeRequest;
import com.tom.first.simple.dto.evaluation.SubjectRequest;
import com.tom.first.simple.dto.user.NameRequest;
import com.tom.first.simple.mapper.EvaluationMapper;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.repository.EvaluationRepository;
import com.tom.first.simple.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {

	private final EvaluationRepository evaluationRepository;
	private final UserRepository userRepository;
	private final EvaluationMapper mapper;

	public List<EvaluationResponse> findAll() {
		List<Evaluation> evaluations = evaluationRepository.findAll();
		if (evaluations.isEmpty()) {
			throw new RuntimeException(String.format("No evaluation found."));
		}
		return evaluations.stream().map(mapper::fromEvaluation).collect(Collectors.toList());
	}

	public EvaluationResponse findBySubject(SubjectRequest request) {
		return evaluationRepository.findBySubject(request.subject()).map(mapper::fromEvaluation)
				.orElseThrow(() -> new RuntimeException(
						String.format("No evaluation found with the provided subject name: %s", request.subject())));
	}

	public List<EvaluationResponse> findByGrade(GradeRequest request) {
		List<Evaluation> evaluations = evaluationRepository.findByGrade(request.grade());
		if (evaluations.isEmpty()) {
			throw new RuntimeException(String.format("No evaluation found for grade: %.2f", request.grade()));
		}
		return evaluations.stream().map(mapper::fromEvaluation).collect(Collectors.toList());
	}

	@Transactional
	public EvaluationResponse createEvaluation(EvaluationRequest request) {
		if (evaluationRepository.existsBySubject(request.subject())) {
			throw new RuntimeException(
					String.format("Evaluation with same name already exists %s", request.subject()));
		}
		var evaluation = evaluationRepository.save(mapper.toEvaluation(request));
		return mapper.fromEvaluation(evaluation);
	}

	@Transactional
	public EvaluationResponse updateEvaluation(SubjectRequest nameRequest, EvaluationUpdate request) {
		var evaluation = evaluationRepository.findBySubject(nameRequest.subject())
				.orElseThrow(() -> new RuntimeException(
						String.format("No evaluation found with the provided subject name: %s", request.subject())));
		var user = userRepository.findByName(request.name()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request.name())));
		mapper.mergeEvaluation(evaluation, request, user);
		evaluationRepository.save(evaluation);
		return mapper.fromEvaluation(evaluation);
	}

	@Transactional
	public void deleteEvaluation(SubjectRequest request) {
		if (!evaluationRepository.existsBySubject(request.subject())) {
			throw new RuntimeException(
					String.format("No evaluation was found with the provided name: %s", request.subject()));
		}
		evaluationRepository.deleteBySubject(request.subject());
	}

	@Transactional
	public void deleteAllUsersFromEvaluation(SubjectRequest request) {
		var evaluation = evaluationRepository.findBySubject(request.subject()).orElseThrow(() -> new RuntimeException(
				String.format("No evaluation was found with the provided name: %s", request.subject())));

		evaluation.setUser(null);
		evaluationRepository.save(evaluation);
	}

	@Transactional
	public void deleteAllEvaluationFromUser(NameRequest request) {
		var user = userRepository.findByName(request.name()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request.name())));

		user.setEvaluations(null);
		userRepository.save(user);
	}

}
