package com.tom.management.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeriadoRequest(
		
		@NotNull(message = "Nome do feriado não pode ficar nulo")
		@NotBlank(message = "Nome do feriado não pode ficar vazio")
		String Nome,
		
		@NotNull(message = "Data não pode ficar nula")
		LocalDate Data) {

}
