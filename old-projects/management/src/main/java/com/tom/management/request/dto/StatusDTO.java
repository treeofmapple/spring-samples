package com.tom.management.request.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tom.management.model.Status;

import jakarta.validation.constraints.NotNull;

public record StatusDTO(
		
		@NotNull(message = "O status da solicitacao nao pode ser nulo")
		@JsonDeserialize(using = StatusDeserializer.class)
		Status Status) {

}
