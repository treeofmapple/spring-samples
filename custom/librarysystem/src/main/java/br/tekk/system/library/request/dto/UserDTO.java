package br.tekk.system.library.request.dto;

import java.util.UUID;

import br.tekk.system.library.model.User;

public record UserDTO(UUID id) {
	public UserDTO(User user) {
		this(user.getId());
	}
}
