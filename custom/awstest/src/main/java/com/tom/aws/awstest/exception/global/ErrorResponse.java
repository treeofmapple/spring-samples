package com.tom.aws.awstest.exception.global;

import java.util.Map;

public record ErrorResponse(Map<String, String> errorResponse) {

}
