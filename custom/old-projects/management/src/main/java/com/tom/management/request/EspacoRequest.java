package com.tom.management.request;

import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EspacoRequest(
		
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
		
		Set<String> Equipamento) {

	

}
