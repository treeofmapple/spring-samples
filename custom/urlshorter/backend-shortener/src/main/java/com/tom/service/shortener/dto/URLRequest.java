package com.tom.service.shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record URLRequest(
		
		@NotNull(message = "URL não pode ser nula")
		@NotBlank(message = "URL não pode estar em branco")
		@Pattern(regexp = "^(https?|ftp)://.+$", message = "A URL deve ser válida (exemplo: http://www.exemplo.com)")
		String url) {

}
