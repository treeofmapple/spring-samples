package br.tekk.system.library.request;

import java.time.LocalDateTime;
import java.util.UUID;

import br.tekk.system.library.request.dto.UserDTOBookItems;

public record UserResponse(UUID id, String username, String email, int age, LocalDateTime date_created, UserDTOBookItems book) {
}