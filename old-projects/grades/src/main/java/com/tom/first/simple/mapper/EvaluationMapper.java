package com.tom.first.simple.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.dto.evaluation.EvaluationUpdate;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.model.User;
import com.tom.first.simple.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationMapper {

	private final UserRepository repository;

	public Evaluation toEvaluation(EvaluationRequest request) {
		if (request == null) {
			return null;
		}

		return repository.findByName(request.name())
				.map(user -> Evaluation.builder().subject(request.subject()).description(request.description())
						.grade(request.grade()).user(user).build())
				.orElseThrow(() -> new RuntimeException("User with requested name doesn't exist: " + request.name()));
	}

	public EvaluationResponse fromEvaluation(Evaluation evaluation) {
		if (evaluation == null) {
			return null;
		}
		return new EvaluationResponse(evaluation.getSubject(), evaluation.getDescription(), evaluation.getGrade(),
				evaluation.getUser());
	}

	public void mergeEvaluation(Evaluation evaluation, EvaluationUpdate request, User user) {
		evaluation.setSubject(request.subject());
		evaluation.setDescription(request.description());
		evaluation.setGrade(request.grade());
		evaluation.setUser(user);
	}

}
