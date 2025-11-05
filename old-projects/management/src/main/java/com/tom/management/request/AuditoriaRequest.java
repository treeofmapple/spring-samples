package com.tom.management.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuditoriaRequest(
		
		Long UsuarioId,
		
		@NotNull(message = "Ação não pode ficar nula")
		@NotBlank(message = "Ação não pode ficar vazia")
		String Acao,
		
		@NotNull(message = "Data não pode ficar nula")
        LocalDateTime Data,

		@NotNull(message = "Detalhes da auditoria não pode ficar nula")
		@NotBlank(message = "Detalhes da auditoria não pode ficar vazia")
		String Detalhes
		
		) {

}
