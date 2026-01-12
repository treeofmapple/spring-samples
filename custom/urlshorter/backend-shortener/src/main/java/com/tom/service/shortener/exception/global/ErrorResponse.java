package com.tom.service.shortener.exception.global;

import java.util.Map;

public record ErrorResponse(Map<String, String> errorResponse) {

}
