package com.tom.first.datajpa.model;

import com.tom.first.datajpa.model.base.BaseEntity;
import com.tom.first.datajpa.model.base.Resource;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Lecture extends BaseEntity {

	private String name;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@OneToOne
	@JoinColumn(name = "resource_id")
	private Resource resource;
}
