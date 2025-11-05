package com.tom.first.simple.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.tom.first.simple.model.Evaluation;

public record UserResponse(String name, String email, EvaluationDTO evaluation) {
    public UserResponse(String name, String email, List<Evaluation> evaluations) {
        this(name, email, EvaluationDTO.transform(evaluations));
    }

    public record EvaluationDTO(List<EvaluationSummary> evaluations) {
        public static EvaluationDTO transform(List<Evaluation> evaluations) {
            return new EvaluationDTO(evaluations == null ? List.of() :
                evaluations.stream().map(EvaluationSummary::from).collect(Collectors.toList()));
        }
    }

    public record EvaluationSummary(String subject, String description, double grade) {
        public static EvaluationSummary from(Evaluation e) {
            return new EvaluationSummary(e.getSubject(), e.getDescription(), e.getGrade());
        }
    }
}