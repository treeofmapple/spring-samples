package com.tom.first.elastic.models.vehicle;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
	CARRO, MOTO;

	@JsonCreator
	public static Type fromString(String value) {
		return Type.valueOf(value.toUpperCase());
	}
}
