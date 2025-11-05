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
// @DiscriminatorValue("FILE")
public class File extends Resource {

	private String type;
}
