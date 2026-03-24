package com.tom.service.datagen.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record SchoolRequest(
		
		@PositiveOrZero
		short aluno,
		
		@PositiveOrZero
		short disciplina,
		
		@PositiveOrZero
		short turma,
		
		@PositiveOrZero
		@Size(min = 2000, max = 2100)
		short year,
		
		@PositiveOrZero
		@Size(min = 1, max = 100)
		short genderPercentage
		
		) {

	
	
}
