package com.tom.service.datagen.model.school;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turmas {

	private Long id;
	private Long id_disciplina;
	private Short ano;
	private Short semestre;
	
}
