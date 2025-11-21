package com.tom.first.simple.dto;

public record EvaluationResponse(
		
		String subject, 
		
		String description, 
		
		double grade, 
		
		String username) {

}
