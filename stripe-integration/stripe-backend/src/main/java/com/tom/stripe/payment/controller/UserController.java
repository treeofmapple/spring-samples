package com.tom.stripe.payment.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;
import com.tom.stripe.payment.user.dto.PageUserResponse;
import com.tom.stripe.payment.user.dto.SimpleUserResponse;
import com.tom.stripe.payment.user.dto.UserRequest;
import com.tom.stripe.payment.user.dto.UserResponse;
import com.tom.stripe.payment.user.dto.UserUpdate;
import com.tom.stripe.payment.user.repository.filtering.UserSortOption;
import com.tom.stripe.payment.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

	private UserService service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageUserResponse searchUserByParam(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(required = false) String username, @RequestParam(required = false) String email,
			@RequestParam(required = false) UserSortOption sortParam) {
		return service.searchUserByParams(page, username, email, sortParam);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public SimpleUserResponse searchUserById(@PathVariable(value = "id") UUID query) {
		return service.searchUserById(query);
	}

	@GetMapping(value = "/auth/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponse searchUserByIdAuthenticated(@PathVariable(value = "id") UUID query) {
		return service.searchUserByIdAuthenticated(query);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse createUser(@RequestBody UserRequest request) {
		return service.createUser(request);
	}

	@PostMapping(value = "/currency/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserResponse setDefaultCurrency(@PathVariable(value = "id") UUID query,
			@RequestParam(value = "curr") AcceptedCurrency currency) {
		return service.setDefaultCurrency(query, currency);
	}

	@PostMapping(value = "/payment/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserResponse setDefaultPaymentMethod(@PathVariable(value = "id") UUID query,
			@RequestParam(value = "pay") PaymentMethods payment) {
		return service.setDefaultPaymentMethod(query, payment);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public UserResponse updateUser(@RequestBody UserUpdate request) {
		return service.updateUser(request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable(value = "id") UUID query) {
		service.deleteUser(query);
	}

}
