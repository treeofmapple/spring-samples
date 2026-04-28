package br.tekk.system.library.request;

import java.time.LocalDate;

public record BookResponse(Integer id, String titulo, String author, int quantity, double price, LocalDate ano) {

}
