package com.tom.awstest.lambda.exception.global;

import java.util.Map;

public record ErrorResponse(Map<String, String> errorResponse) {

}
