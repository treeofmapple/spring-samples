package com.tom.first.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.library.dto.UserRequest;
import com.tom.first.library.dto.UserRequest.EmailRequest;
import com.tom.first.library.dto.UserRequest.NameRequest;
import com.tom.first.library.dto.UserRequest.PasswordRequest;
import com.tom.first.library.dto.UserResponse;
import com.tom.first.library.dto.UserResponse.UserUpdateResponse;
import com.tom.first.library.mapper.UserMapper;
import com.tom.first.library.model.User;
import com.tom.first.library.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService  {

	private final UserRepository repository;
	private final UserMapper mapper;

	public List<UserResponse> findAll() {
		List<User> users = repository.findAll();
		if (users.isEmpty()) {
			throw new RuntimeException(String.format("No users found."));
		}
		return users.stream().map(mapper::fromUser).collect(Collectors.toList());
	}

	public List<UserResponse> findByUsername(NameRequest request) {
		List<User> users = repository.findAllByUsername(request.username());
		if (users.isEmpty()) {
			throw new RuntimeException
			(String.format("No user with username %s found.", request.username()));
		}
		return users.stream().map(mapper::fromUser).collect(Collectors.toList());
	}

	public UserResponse findByEmail(EmailRequest request) {
		var user = repository.findByEmail(request.email())
				.map(mapper::fromUser)
				.orElseThrow(
				() -> new RuntimeException
				(String.format("No user found with the provided email: %s", request.email())));
		return user;
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		if (repository.existsByEmail(request.email())) {
			throw new RuntimeException
			(String.format("User with same name already exists %s", request.email()));
		}
		var user = repository.save(mapper.toUser(request));
		return mapper.fromUser(user);
	}

	@Transactional
	public UserUpdateResponse updateUser(EmailRequest mail, UserRequest request) {
		var user = repository.findByEmail(mail.email()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided name: %s", request.username())));
		mapper.mergeUser(user, request);
		repository.save(user);
		return mapper.fromUpdateResponse(user);
	}

	@Transactional
	public void passwordUpdate(EmailRequest mail, PasswordRequest request) {
		var user = repository.findByEmail(mail.email()).orElseThrow(
				() -> new RuntimeException(String.format("No user found with the provided email: %s", mail.email())));
		mapper.mergePassword(user, request);
		repository.save(user);
	}

	@Transactional
	public void deleteUserByEmail(EmailRequest request) {
		if (!repository.existsByEmail(request.email())) {
			throw new RuntimeException(String.format("No user found with the provided name: %s", request));
		}
		repository.deleteByEmail(request.email());
	}

}
