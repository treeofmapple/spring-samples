package com.tom.first.elastic.models.library;

import java.time.LocalDate;

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
@Document(indexName = "book")
public class BookDocument {

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String title;
	
	@Field(type = FieldType.Text, analyzer = "standard")
	private String author;
	
	@Field(type = FieldType.Integer)
	private Integer quantity;

	@Field(type = FieldType.Date)
	private LocalDate launchYear;
	
}
