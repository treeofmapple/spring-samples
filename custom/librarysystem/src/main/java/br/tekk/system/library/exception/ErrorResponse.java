package br.tekk.system.library.exception;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
