package com.tom.first.elastic.models.vehicle;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
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
@Document(indexName = "vehicles")
public class VehicleDocument {

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String brand;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String model;

	@Field(type = FieldType.Keyword)
	private String color;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String plate;

	@Field(type = FieldType.Keyword)
	private Type type;

	@Field(type = FieldType.Date, format = DateFormat.date_time)
	private ZonedDateTime createdAt;
}
