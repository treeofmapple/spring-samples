package com.tom.management.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRequest(
		
		@NotBlank(message = "Nome do equipamento não pode ficar vazio")
		@NotNull(message = "Nome do equipamento não pode ficar nulo")
		String Nome) {

}
