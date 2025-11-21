package com.tom.first.simple.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tom.first.simple.model.Evaluation;

@Component
public class MathLogic {

	public double mathAverageGrade(List<Evaluation> evaluations) {
		double sumGrades = evaluations.stream().mapToDouble(Evaluation::getGrade).sum();
		return sumGrades / evaluations.size();
	}

	
	/*
	public UserGradeResponse mathAverageGrade(User user, UserMapper mapper) {
		List<Evaluation> evaluations = user.getEvaluations();
		double averageGrade = 0.0;
		if (!evaluations.isEmpty()) {
			averageGrade = mathAverageGrade(evaluations);
		}
		return mapper.fromUserGrade(user.getName(), averageGrade);
	}
	*/
	
}
