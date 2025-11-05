package com.tom.first.datajpa.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	private String streetName;
	
	private String houseNumber;
	
	private String zipCode;
	
}
