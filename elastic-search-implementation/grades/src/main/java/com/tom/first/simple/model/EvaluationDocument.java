package com.tom.first.simple.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "evaluations")
public class EvaluationDocument {

	@Id
	private String id;
	
	@Field(type = FieldType.Text, analyzer = "standard")
	private String subject;
	
	@Field(type = FieldType.Text, analyzer = "standard")
	private String description;
	
	@Field(type = FieldType.Double)
	private Double grade;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String username;
}
