package com.tom.service.datagen.model.school;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notas {

	private Long id;
	private Long id_avaliacao; 
	private BigDecimal nota;
	
}
