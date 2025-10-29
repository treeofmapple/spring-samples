package com.tom.auth.monolithic.exception;

import java.util.Map;

public record ErrorResponse(Map<String, String> errorResponse) {

}
