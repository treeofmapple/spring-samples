package com.tom.first.library.dto;

import java.time.LocalDate;

import com.tom.first.library.model.enums.Status;

public record BookItemResponse(
		
		String username,
		Status status,
		LocalDate rentStart,
		LocalDate rentEnd,
		String bookTitle,
		Double price
		
		) {

}
