package com.tom.first.elastic.models.user;

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
@Document(indexName = "usernames")
public class UserDocument {

	@Id
	private String id;
	
	@Field(type = FieldType.Text, analyzer = "standard")
	private String name;
	
	@Field(type = FieldType.Text, analyzer = "standard")
	private String email;
	
}
