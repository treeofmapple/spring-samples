package com.tom.reactive.first.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.reactive.first.dto.PageUserResponse;
import com.tom.reactive.first.dto.UserRequest;
import com.tom.reactive.first.dto.UserResponse;
import com.tom.reactive.first.dto.UserUpdateRequest;
import com.tom.reactive.first.exception.sql.DataViolationException;
import com.tom.reactive.first.exception.sql.NotFoundException;
import com.tom.reactive.first.mapper.UserMapper;
import com.tom.reactive.first.model.User;
import com.tom.reactive.first.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final UserRepository repository;
	private final UserMapper mapper;
	private final R2dbcEntityTemplate entityTemplate;

	@Transactional(readOnly = true)
	public Mono<UserResponse> searchUserById(UUID userId) {
		return repository.findById(userId).map(mapper::toResponse).switchIfEmpty(
				Mono.error(new NotFoundException(String.format("User with ID: %s was not found.", userId))));
	}

	@Transactional(readOnly = true)
	public Mono<PageUserResponse> searchUserByParams(int page, String nickname, String email, String role) {
		User probe = User.builder().nickname(nickname).email(email).role(role).build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return repository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.collectList().map(list -> mapper.toResponse(list, page, PAGE_SIZE));
	}

	@Transactional
	public Mono<UserResponse> createUser(UserRequest request) {
		return repository.findByEmailOrNickname(request.nickname(), request.email()).flatMap(existentUser -> {
			return Mono.<User>error(new DataViolationException(String
					.format("User with nickname %s or mail %s already exists.", request.nickname(), request.email())));
		}).switchIfEmpty(Mono.defer(() -> {
			User user = mapper.build(request);
			return entityTemplate.insert(user);
		})).map(mapper::toResponse);
	}

	@Transactional
	public Mono<UserResponse> updateUserInfo(UserUpdateRequest request) {
		return repository.findById(request.userId())
				.switchIfEmpty(Mono.error(
						new NotFoundException(String.format("User with ID: %s was not found.", request.userId()))))
				.flatMap(existentUser -> {
					mapper.update(existentUser, request);
					existentUser.setId(request.userId());
					return entityTemplate.update(existentUser);
				}).map(mapper::toResponse);
	}

	@Transactional
	public Mono<Void> deleteUser(UUID userId) {
		return repository.existsById(userId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return repository.deleteById(userId);
			}
			return Mono.error(new NotFoundException(String.format("User with ID: %s was not found.", userId)));
		});
	}

}
