package br.tekk.system.library.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

		@NotBlank(message = "Username não pode ficar vazio")
		@NotNull(message = "Username não pode ficar nulo")
		String username,

		@Min(value = 4, message = "Idade minima 4") 
		@NotNull(message = "Idade não pode ficar nula")
		int age,

		@NotBlank(message = "Email is required") 
		@NotNull(message = "Email não pode ficar nulo")
		@Email(message = "Email precisa ser valido") 
		String email,

		@Size(min = 6, message = "Quantidade minima de caracteres é 6") 
		@NotNull(message = "Senha não pode ficar nula") 
		String password
		
) {

}