package com.tom.stripe.payment.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.stripe.payment.logic.payment.StripePayment;
import com.tom.stripe.payment.logic.security.SecurityUtils;
import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;
import com.tom.stripe.payment.user.component.UserComponent;
import com.tom.stripe.payment.user.dto.PageUserResponse;
import com.tom.stripe.payment.user.dto.PostalInfoRequest;
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
	
	private final StripePayment stripePayment;

	@Transactional(readOnly = true)
	public PageUserResponse searchUserByParams(int page, String username, String email, UserSortOption sortParam) {
		var finalSort = userSort.selectUserSort(sortParam);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, finalSort);
		Specification<User> spec = UserSpecification.findByCriteria(username, email);
		var users = repository.findAll(spec, pageable);
		return mapper.toResponse(users);
	}

	@Transactional(readOnly = true)
	public SimpleUserResponse searchUserById(UUID userId) {
		var user = component.findById(userId);
		return mapper.toSimpleResponse(user);
	}

	@Transactional(readOnly = true)
	public UserResponse searchUserByIdAuthenticated(UUID userId) {
		var user = component.findById(userId);
		return mapper.toResponse(user);
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is creating user: {}", userIp, request.nickname());

		component.ensureNicknameAndEmailAreUnique(request.nickname(), request.email());

		var user = mapper.build(request);
		var customerId = stripePayment.createStripeCustomer(user);
		user.setStripeCustomerId(customerId);
		
		var userCreated = repository.save(user);
		log.info("IP: {}, created user: {}", userIp, request.nickname());
		return mapper.toResponse(userCreated);
	}

	@Transactional
	public UserResponse updateUser(UserUpdate request) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is updating user: {}", userIp, request.nickname());

		var user = component.findById(request.userId());

		component.checkIfNicknameAlreadyUsed(user, request.nickname());
		component.checkIfEmailAlreadyUsed(user, request.email());

		mapper.update(user, request);
		stripePayment.updateStripeCustomer(user.getStripeCustomerId(), user);

		var userEdited = repository.save(user);
		log.info("IP: {}, updated user: {}", userIp, request.nickname());
		return mapper.toResponse(userEdited);
	}
	
	@Transactional 
	public UserResponse fillUserData(PostalInfoRequest request) {
		var user = component.findById(request.userId());
		mapper.update(user, request);
		stripePayment.updateStripeCustomer(user.getStripeCustomerId(), user);
		var userEdited = repository.save(user);
		return mapper.toResponse(userEdited);
	}

	@Transactional
	public UserResponse setDefaultCurrency(UUID userId, AcceptedCurrency currency) {
		var user = component.findById(userId);
		AcceptedCurrency.isValid(currency);
	    user.setCurrencyPreferred(currency);
		var userSaved = repository.save(user);
		return mapper.toResponse(userSaved);
	}

	@Transactional
	public UserResponse setDefaultPaymentMethod(UUID userId, PaymentMethods paymentMethod) {
		var user = component.findById(userId);
		PaymentMethods.isValid(paymentMethod);
	    user.setDefaultPaymentMethods(paymentMethod);
		var userSaved = repository.save(user);
		return mapper.toResponse(userSaved);
	}
	
	/*
	 * revoking user access is better, but i don't had implemented that, but this is
	 * more drastic, just for testing also
	 */
	
	@Transactional
	public void deleteUser(UUID userId) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is deleting user with id: {}", userIp, userId);

		var user = component.findById(userId);
		stripePayment.deleteStripeCustomer(user.getStripeCustomerId());
		repository.delete(user);

		log.info("IP: {}, deleted user with id: {}", userIp, userId);
	}

}
