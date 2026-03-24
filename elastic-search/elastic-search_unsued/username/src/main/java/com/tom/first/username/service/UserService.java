package com.tom.first.username.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.username.dto.UserRequest;
import com.tom.first.username.dto.UserResponse;
import com.tom.first.username.mapper.UserMapper;
import com.tom.first.username.processes.events.UserCreatedEvent;
import com.tom.first.username.processes.events.UserDeletedEventByEmail;
import com.tom.first.username.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	private final ApplicationEventPublisher eventPublisher;
	private final UserRepository repository;
	private final UserMapper mapper;

	@Transactional(readOnly = true)
	public UserResponse findById(long query) {
		return repository.findById(query).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("User with id '%s' was not found.", query));
		});
	}

	@Transactional(readOnly = true)
	public UserResponse findByUsername(String username) {
		return repository.findByEmail(username).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("User with username '%s' was not found.", username));
		});
	}

	@Transactional(readOnly = true)
	public UserResponse findByEmail(String email) {
		return repository.findByEmail(email).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException(String.format("User with email '%s' was not found.", email));
		});
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		if (repository.existsByEmail(request.email())) {
			throw new RuntimeException(String.format("User with same email already exists %s", request.email()));
		}
		var user = mapper.build(request);
		repository.save(user);

		eventPublisher.publishEvent(new UserCreatedEvent(user));
		return mapper.toResponse(user);
	}

	@Transactional
	public void deleteUserById(long userId) {
		var user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User Id not Found"));

		eventPublisher.publishEvent(new UserDeletedEventByEmail(user.getEmail()));
		repository.deleteById(userId);
	}

	@Transactional
	public void deleteUserByEmail(String email) {
		var user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User email not Found"));

		eventPublisher.publishEvent(new UserDeletedEventByEmail(user.getEmail()));
		repository.deleteByEmail(email);
	}
}