package com.tom.first.elastic.models.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
	CAR, MOTORCYCLE;

	@JsonCreator
	public static Type fromString(String value) {
		return Type.valueOf(value.toUpperCase());
	}
}
