package com.tom.first.establishment.mapper;

import org.springframework.stereotype.Component;

import com.tom.first.establishment.dto.EstablishmentRequest;
import com.tom.first.establishment.dto.EstablishmentResponse;
import com.tom.first.establishment.dto.EstablishmentUpdate;
import com.tom.first.establishment.model.Establishment;

@Component
public class EstablishmentMapper {

	public Establishment toEstablishment(EstablishmentRequest request) {
		if(request == null) {
			return null;
		}
		
		return Establishment.builder()
				.name(request.name())
				.cnpj(request.cnpj())
				.address(request.address())
				.telephone(request.phone())
				.vacanciesMotorcycles(request.motorcycleSpotCount())
				.vacanciesCars(request.carSpotCount())
				.build();
		
	}
	
	public EstablishmentResponse fromEstablishment(Establishment request) {
		return new EstablishmentResponse(
				request.getName(),
				request.getCnpj(),
				request.getAddress(),
				request.getTelephone(),
				request.getVacanciesMotorcycles(),
				request.getVacanciesCars()
				);
	}
	
	public void mergeEstablishment(Establishment place, EstablishmentUpdate request) {
		place.setName(request.name());
		place.setCnpj(request.cnpj());
		place.setAddress(request.address());
		place.setTelephone(request.phone());
		place.setVacanciesMotorcycles(request.motorcycleSpotCount());
		place.setVacanciesCars(request.carSpotCount());
	}
	
}
