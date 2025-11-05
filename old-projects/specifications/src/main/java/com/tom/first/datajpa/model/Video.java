package com.tom.first.datajpa.model;

import com.tom.first.datajpa.model.base.Resource;

import jakarta.persistence.Entity;
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
// @DiscriminatorValue("VIDEO")
// @Polymorphism(type = PolymorphismType.EXPLICIT)
// @PrimaryKeyJoinColumn(name = "video_id") --> When not TABLE_PER_CLASS
public class Video extends Resource {

	private int length;
	
}

