package br.tekk.system.library.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.tekk.system.library.exception.UserAlreadyExistsException;
import br.tekk.system.library.exception.UserNotFoundException;
import br.tekk.system.library.mapper.UserMapper;
import br.tekk.system.library.model.User;
import br.tekk.system.library.repository.UserRepository;
import br.tekk.system.library.request.UserRequest;
import br.tekk.system.library.request.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	private final UserMapper mapper;

	public List<UserResponse> findAllUsers() {
		List<User> users = repository.findAll();
		if (users.isEmpty()) {
			throw new UserNotFoundException("No users found.");
		}
		return users.stream().map(mapper::fromUser).collect(Collectors.toList());
	}

	public UserResponse findById(UUID userId) {
		return repository.findById(userId).map(mapper::fromUser).orElseThrow(
				() -> new UserNotFoundException(String.format("No user found with the provided ID:: %s", userId)));
	}

	public UUID createUser(UserRequest request) {
		if (repository.existsByEmail(request.email())) {
			throw new UserAlreadyExistsException(String.format("User with same email already exists %s", request.email()));
		}
		var user = repository.save(mapper.toUser(request));
		return user.getId();
	}

	public void updateUser(UUID userId, UserRequest request) {
		var user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException(
				String.format("Cannot update User, no user found with the provided ID:: %s", userId)));
		mergerUser(user, request);
		repository.save(user);
	}  
	
	public void deleteUser(UUID userId) {
		if (!repository.existsById(userId)) {
			throw new UserNotFoundException("User not found with ID:: " + userId);
		}
		repository.deleteById(userId);
	}
	
	private void mergerUser(User user, UserRequest request) {
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(request.password());
		user.setAge(request.age());
	}

}
