package br.tekk.system.library.request.dto;

import java.time.LocalDate;

public record BookItemSummary(Integer id, String title, LocalDate rentStart, LocalDate rentEnd) {
}