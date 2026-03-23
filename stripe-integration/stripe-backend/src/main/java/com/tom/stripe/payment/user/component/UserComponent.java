package com.tom.stripe.payment.user.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tom.stripe.payment.exception.sql.DataViolationException;
import com.tom.stripe.payment.exception.sql.NotFoundException;
import com.tom.stripe.payment.user.model.User;
import com.tom.stripe.payment.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserComponent {

	private final UserRepository repository;

	public User findById(UUID userId) {
		return repository.findById(userId)
				.orElseThrow(() -> new NotFoundException(String.format("User with id %s, was not found.", userId)));
	}

	public void ensureNicknameAndEmailAreUnique(String nickname, String email) {
		if (repository.existsByNickname(nickname)) {
			throw new DataViolationException(String.format("Nickname is already taken: %s", nickname));
		}

		if (repository.existsByEmail(email)) {
			throw new DataViolationException(String.format("Email is already taken: %s", email));
		}
	}

	public void checkIfEmailAlreadyUsed(User currentUser, String newEmail) {
		if (repository.isEmailTakenByAnotherUser(newEmail, currentUser.getId())) {
			throw new DataViolationException(String.format("Email is already in use by another account: %s", newEmail));
		}
	}

	public void checkIfNicknameAlreadyUsed(User currentUser, String newUsername) {
		if (repository.isNicknameTakenByAnotherUser(newUsername, currentUser.getId())) {
			throw new DataViolationException(
					String.format("Nickname is already taken by another account: %s", newUsername));
		}
	}

}
