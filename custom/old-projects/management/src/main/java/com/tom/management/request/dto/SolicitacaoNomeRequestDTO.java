package com.tom.management.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoNomeRequestDTO(
		
		@NotNull(message = "Nome da solicitacao não pode ser nulo")
		@NotBlank(message = "Nome da solitacacao não pode ficar vazio")
		String Nome
		) {

}
