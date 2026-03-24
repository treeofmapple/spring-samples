package com.tom.management.mapper;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.tom.management.model.Equipamento;
import com.tom.management.model.EspacoFisico;
import com.tom.management.repository.EquipamentoRepository;
import com.tom.management.request.EspacoRequest;
import com.tom.management.request.EspacoResponse;
import com.tom.management.request.dto.EspacoRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EspacoMapper {

	private final EquipamentoRepository repository;

	public EspacoFisico toEspaco(EspacoRequest request) {
		if (request == null) {
			return null;
		}

		Set<String> equipamentosRequest = request.Equipamento();
		if (equipamentosRequest == null || equipamentosRequest.isEmpty()) {
			Set<Equipamento> equipamentos = null;

			return EspacoFisico.builder().nome(request.Nome()).tipo(request.Tipo()).metragem(request.Metragem())
					.equipamentos(equipamentos).build();
		}

		if (!repository.existsByNomeIn(equipamentosRequest)) {
			throw new RuntimeException(
					String.format("Equipamento com o nome requisitado não existe: %s", equipamentosRequest));
		}

		var equipamentos = repository.findByNomeIn(equipamentosRequest);

		return EspacoFisico.builder().nome(request.Nome()).tipo(request.Tipo()).metragem(request.Metragem())
				.equipamentos(equipamentos).build();
	}

	public EspacoFisico toEspacoDTO(EspacoRequestDTO request) {
		if (request == null) {
			return null;
		}

		Set<String> equipamentosRequest = request.Equipamento();
		if (equipamentosRequest.isEmpty() || !repository.existsByNomeIn(equipamentosRequest)) {
			throw new RuntimeException(
					String.format("Equipamento com o nome requisitado não existe: %s", equipamentosRequest));
		}

		var equipamentos = repository.findByNomeIn(equipamentosRequest);

		return EspacoFisico.builder().nome(request.Nome()).tipo(request.Tipo()).metragem(request.Metragem())
				.disponibilidade(request.Disponibilidade()).equipamentos(equipamentos).build();
	}

	public EspacoResponse fromEspaco(EspacoFisico espaco) {
		return new EspacoResponse(espaco.getId(), espaco.getNome(), espaco.getTipo(), espaco.getMetragem(),
				espaco.getDisponibilidade(), espaco.getEquipamentos());
	}

}
