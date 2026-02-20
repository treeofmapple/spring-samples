package com.tom.stripe.payment.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.stripe.payment.exception.sql.NotFoundException;
import com.tom.stripe.payment.logic.security.SecurityUtils;
import com.tom.stripe.payment.user.component.UserComponent;
import com.tom.stripe.payment.user.dto.PageUserResponse;
import com.tom.stripe.payment.user.dto.SimpleUserResponse;
import com.tom.stripe.payment.user.dto.UserRequest;
import com.tom.stripe.payment.user.dto.UserResponse;
import com.tom.stripe.payment.user.dto.UserUpdate;
import com.tom.stripe.payment.user.mapper.UserMapper;
import com.tom.stripe.payment.user.model.User;
import com.tom.stripe.payment.user.repository.UserRepository;
import com.tom.stripe.payment.user.repository.UserSpecification;
import com.tom.stripe.payment.user.repository.filtering.UserSortOption;
import com.tom.stripe.payment.user.repository.filtering.UserSortParameter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	@Value("${application.size.page:20}")
	private int PAGE_SIZE;

	private final UserRepository repository;
	private final UserMapper mapper;
	private final UserSortParameter userSort;

	private final UserComponent component;

	private final SecurityUtils security;

	@Transactional(readOnly = true)
	public PageUserResponse searchUserByParams(int page, String username, String email, UserSortOption sortParam) {
		var finalSort = userSort.selectUserSort(sortParam);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, finalSort);
		Specification<User> spec = UserSpecification.findByCriteria(username, email);
		var users = repository.findAll(spec, pageable);
		return mapper.toResponse(users);
	}

	@Transactional(readOnly = true)
	public SimpleUserResponse searchById(UUID userId) {
		var user = component.findById(userId);
		return mapper.toSimpleResponse(user);
	}

	@Transactional(readOnly = true)
	public UserResponse searchByIdAuthenticated(UUID userId) {
		var user = component.findById(userId);
		return mapper.toResponse(user);
	}

	@Transactional
	public UserResponse createByUser(UserRequest request) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is creating user: {}", userIp, request.nickname());

		component.ensureNicknameAndEmailAreUnique(request.nickname(), request.email());

		var user = mapper.build(request);
		var userCreated = repository.save(user);
		log.info("IP: {}, created user: {}", userIp, request.nickname());
		return mapper.toResponse(userCreated);
	}

	// update
	@Transactional
	public UserResponse updateUser(UserUpdate request) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is updating user: {}", userIp, request.nickname());

		var user = component.findById(request.userId());

		component.checkIfNicknameAlreadyUsed(user, request.nickname());
		component.checkIfEmailAlreadyUsed(user, request.email());

		mapper.update(user, request);

		var userEdited = repository.save(user);
		log.info("IP: {}, updated user: {}", userIp, request.nickname());
		return mapper.toResponse(userEdited);
	}

	@Transactional
	public void deleteUser(UUID userId) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is deleting user with id: {}", userIp, userId);

		if (!repository.existsById(userId)) {
			throw new NotFoundException(String.format("User with id: %s, was not found.", userId));
		}
		repository.deleteById(userId);

		log.info("IP: {}, deleted user with id: {}", userIp, userId);
	}

}
