package com.tom.first.datajpa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tom.first.datajpa.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries(
		{ 
			@NamedQuery(
					name = "Author.findByNamedQuery", 
					query = "SELECT a FROM Author a WHERE a.age >= :age"),
			@NamedQuery(
					name = "Author.updateByNamedQuery", 
					query = "UPDATE Author a SET a.age = :age") 
			}
		)
// @Table(name = "AUTHOR_TBL")
public class Author extends BaseEntity {

	private String firstName;

	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	private int age;

	@ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Course> courses;

}
