package com.tom.first.establishment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.establishment.dto.EstablishmentRequest;
import com.tom.first.establishment.dto.EstablishmentResponse;
import com.tom.first.establishment.dto.EstablishmentUpdate;
import com.tom.first.establishment.mapper.EstablishmentMapper;
import com.tom.first.establishment.model.Establishment;
import com.tom.first.establishment.repository.EstablishmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

	private final EstablishmentRepository repository;
	private final EstablishmentMapper mapper;

	public List<EstablishmentResponse> findAll() {
		List<Establishment> establishment = repository.findAll();
		if (establishment.isEmpty()) {
			throw new RuntimeException("No establishments were found.");
		}
		return establishment.stream().map(mapper::fromEstablishment).collect(Collectors.toList());
	}

	public EstablishmentResponse findByName(String name) {
		return repository.findByName(name)
				.map(mapper::fromEstablishment)
				.orElseThrow(() -> {
					return new RuntimeException(String.format("Establishment with name '%s' was not found.", name));
				});
	}

	@Transactional
	public EstablishmentResponse createEstablishment(EstablishmentRequest request) {
		if (repository.existsByNameAndCnpj(request.name(), request.cnpj())) {
			throw new RuntimeException(
					String.format("An establishment with name '%s' and CNPJ '%s' already exists.", request.name(), request.cnpj())
			);
		}

		var establishment = mapper.toEstablishment(request);
		repository.save(establishment);
		return mapper.fromEstablishment(establishment);
	}

	@Transactional
	public EstablishmentResponse updateEstablishment(String name, EstablishmentUpdate request) {

		var establishment = repository.findByName(name)
				.orElseThrow(() -> {
					return new RuntimeException(String.format("Establishment with name '%s' was not found.", name));
				});

		mapper.mergeEstablishment(establishment, request);
		repository.save(establishment);
		return mapper.fromEstablishment(establishment);
	}

	@Transactional
	public void deleteEstablishment(String name) {
		if (!repository.existsByName(name)) {
			throw new RuntimeException(String.format("Establishment with name '%s' does not exist.", name));
		}
		
		repository.deleteByName(name);
	}
}
