package br.tekk.system.library.request;

import java.time.LocalDate;

import br.tekk.system.library.model.Status;
import br.tekk.system.library.request.dto.BookDTO;
import br.tekk.system.library.request.dto.UserDTO;

public record BookItemResponse(Integer id, BookDTO book, UserDTO user, Status status, LocalDate rentStart, LocalDate rentEnd) {

}
