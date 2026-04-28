package br.tekk.system.library.request;

import java.time.LocalDate;
import java.util.UUID;

import br.tekk.system.library.model.Status;
import jakarta.validation.constraints.NotNull;

// @CPF
// @CNPJ

public record BookItemRequest(
		
		@NotNull(message = "Livro não pode ser nulo") 
		Integer book,

		@NotNull(message = "Usuario não pode ser nulo") 
		UUID user,

		@NotNull(message = "Status não pode ser nulo") 
		Status status,

		LocalDate rentStart,
        LocalDate rentEnd

) {
	
    public BookItemRequest(Integer book, UUID user, Status status) {
        this(book, user, status, null, null);
    }

}
