package com.tom.first.simple.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.simple.dto.UserResponse;
import com.tom.first.simple.mapper.UserMapper;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.repository.EvaluationRepository;
import com.tom.first.simple.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenerateData {

	private final EvaluationRepository evaluationRepository;
	private final GenerateDataUtil dataUtil;
	private final UserMapper mapper;

	// Webflux
	private final UserService userService;

	@Transactional
	public Evaluation processEvaluation() {
		var gen = genEvaluation();
		evaluationRepository.save(gen);
		return gen;
	}

	private UserResponse genUser() {
		String username = dataUtil.generateUsername();
		String password = dataUtil.generatePassword();
		String email = dataUtil.generateEmail();
		var userRequest = mapper.build(username, password, email);
		var response = userService.createUser(userRequest);
		return response;
	}

	private Evaluation genEvaluation() {
		var user = genUser();
		Evaluation evaluation = new Evaluation();
		evaluation.setSubject(dataUtil.generateSubject());
		evaluation.setDescription(dataUtil.generateDescription());
		evaluation.setGrade(dataUtil.getRandomDouble(0, 10));
		evaluation.setUserId(user.id());
		return evaluation;
	}
}
