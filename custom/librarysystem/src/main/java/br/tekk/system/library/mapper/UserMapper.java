package br.tekk.system.library.mapper;

import org.springframework.stereotype.Service;

import br.tekk.system.library.model.User;
import br.tekk.system.library.request.UserRequest;
import br.tekk.system.library.request.UserResponse;
import br.tekk.system.library.request.dto.UserDTOBookItems;

@Service
public class UserMapper {

	public User toUser(UserRequest request) {
		if (request == null) {
			return null;
		}
		return User.builder().username(request.username()).age(request.age()).email(request.email())
				.password(request.password()).build();

	}

	public UserResponse fromUser(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getAge(),
				user.getDate_created(), UserDTOBookItems.fromBookItems(user.getBookItems()));
	}
}
