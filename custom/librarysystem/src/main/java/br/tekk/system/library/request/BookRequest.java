package br.tekk.system.library.request;


import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BookRequest(

		@NotBlank(message = "Titulo não pode ser vazio")
		@NotNull(message = "Título não pode ser nulo") 
		String title,

		@NotBlank(message = "Autor não pode ser vazio, caso não tenha autor, deve-se colocar anonimo.")
		String author,
		
		@Min(value = 1, message = "Quantidade minima é de 1 unidade") 
		@Max(value = 100, message = "Quantidade maxima é de 100 unidades")
		int quantity,
		
		@PositiveOrZero(message = "O preço deve ser zero ou positivo") 
		double price,

		@NotNull(message = "Ano não pode ser nulo") 
		LocalDate ano

) {

}
