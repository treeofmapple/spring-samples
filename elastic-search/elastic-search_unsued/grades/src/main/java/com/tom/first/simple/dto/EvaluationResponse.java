package com.tom.first.simple.dto;

public record EvaluationResponse(
		
		String subject, 
		String description, 
		Double grade, 
		String username) {

}
