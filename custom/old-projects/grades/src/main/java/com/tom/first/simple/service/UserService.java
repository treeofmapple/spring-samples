package com.tom.first.simple.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.simple.common.UserUtils;
import com.tom.first.simple.dto.UserRequest;
import com.tom.first.simple.dto.UserResponse;
import com.tom.first.simple.dto.user.NameRequest;
import com.tom.first.simple.dto.user.UserGradeResponse;
import com.tom.first.simple.dto.user.UserUpdateResponse;
import com.tom.first.simple.mapper.UserMapper;
import com.tom.first.simple.model.Evaluation;
import com.tom.first.simple.model.User;
import com.tom.first.simple.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final UserMapper mapper;
	private final UserUtils utils;

	public List<UserResponse> findAll() {
		List<User> users = repository.findAllUsersWithEvaluations();
		if (users.isEmpty()) {
			throw new RuntimeException(String.format("No users found."));
		}
		return users.stream().map(mapper::fromUser).collect(Collectors.toList());
	}

	public UserResponse findByName(NameRequest request) {
		var user = repository.findByName(request.name()).map(mapper::fromUser).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request)));
		return user;
	}

	public UserGradeResponse averageUserGrade(NameRequest request) {
		var user = repository.findByName(request.name()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request.name())));
		List<Evaluation> evaluations = user.getEvaluations();
		if (evaluations.isEmpty()) {
			throw new RuntimeException(String.format("Evaluations is empty"));
		}
		double average = utils.mathAverageGrade(evaluations);
		return mapper.fromUserGrade(user.getName(), average);
	}

	@Transactional
	public List<UserGradeResponse> allAverageUserGrades() {
		List<User> users = repository.findAll();
		if (users.isEmpty()) {
			throw new RuntimeException(String.format("No users found."));
		}
		return users.stream().map(user -> utils.mathAverageGrade(user, mapper)).collect(Collectors.toList());
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		if (repository.existsByEmail(request.email())) {
			throw new RuntimeException(String.format("User with same name already exists %s", request.name()));
		}
		var user = repository.save(mapper.toUser(request));
		return mapper.fromUser(user);
	}

	@Transactional
	public UserUpdateResponse updateUser(NameRequest nameRequest, UserRequest request) {
		var user = repository.findByName(nameRequest.name()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request.name())));
		mapper.mergeUser(user, request);
		repository.save(user);
		return mapper.fromUserUpdate(user);
	}

	@Transactional
	public void deleteUser(NameRequest request) {
		if (!repository.existsByName(request.name())) {
			throw new RuntimeException(String.format("No user found with the provided name: %s", request));
		}
		repository.deleteByName(request.name());
	}
}
