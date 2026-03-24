package com.tom.management.request.dto;

import java.util.Set;

import com.tom.management.model.Disponibilidade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EspacoRequestDTO(
		
		@NotBlank(message = "Nome do espaco não pode ficar vazio")
		@NotNull(message = "Nome do espaco não pode ficar nulo")
		String Nome, 
		
		@NotBlank(message = "Tipo não pode ficar vazio")
		@NotNull(message = "Tipo não pode ficar nulo")
		String Tipo, 
		
		@NotNull(message = "Metragem não pode ficar nulo")	
		@Min(value = 3, message = "Minimo é 3 metros")
		@Max(value = 100, message = "Minimo é 3 metros")
		double Metragem, 
		
		@NotBlank(message = "Disponibilidade não pode ficar vazia")
		@NotNull(message = "Disponibilidade não pode ficar nulo")
		Disponibilidade Disponibilidade,
		
		@NotEmpty(message = "Nome não pode ficar vazio")
		@NotNull(message = "Nome não pode ficar nulo")
		Set<String> Equipamento
		
		){

}
