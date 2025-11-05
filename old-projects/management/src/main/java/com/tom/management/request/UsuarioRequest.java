package com.tom.management.request;

import java.util.Set;

import com.tom.management.model.Perfil;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
		
		@NotBlank(message = "Nome do usuario não pode ficar vazio")
		@NotNull(message = "Nome do usuario não pode ficar nulo")
		String Nome, 
		
		@NotBlank(message = "Email não pode ficar vazio")
		@NotNull(message = "Email não pode ficar nulo")
		@Email(message = "Email não é valido")
		String Email,
		
		@NotEmpty(message = "Perfil não pode ficar vazio")
		@NotNull(message = "Perfil não pode ficar nulo")
		Set<Perfil> Perfil
		
		) {

}
