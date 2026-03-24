package com.tom.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.management.config.LogAuditoria;
import com.tom.management.mapper.ServicosMapper;
import com.tom.management.model.Avaliador;
import com.tom.management.model.Equipamento;
import com.tom.management.model.Feriado;
import com.tom.management.repository.AvaliadorRepository;
import com.tom.management.repository.EquipamentoRepository;
import com.tom.management.repository.FeriadoRepository;
import com.tom.management.request.AvaliadorResponse;
import com.tom.management.request.EquipamentoRequest;
import com.tom.management.request.EquipamentoResponse;
import com.tom.management.request.FeriadoRequest;
import com.tom.management.request.FeriadoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicosGeraisService {

	public final EquipamentoRepository equipamentoRepository;
	public final FeriadoRepository feriadoRepository;
	public final AvaliadorRepository avaliadorRepository;
	public final ServicosMapper mapper;

	@LogAuditoria(acao = "FIND_ALL_AVALIADORES")
	public List<AvaliadorResponse> findAllAvaliador() {
		List<Avaliador> avaliador = avaliadorRepository.findAll();
		if (avaliador.isEmpty()) {
			throw new RuntimeException(String.format("Nenhum avaliador foi encontrado"));
		}
		return avaliador.stream().map(mapper::fromAvaliador).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_ALL_FERIADOS")
	public List<FeriadoResponse> findAllFeriados() {
		List<Feriado> feriado = feriadoRepository.findAll();
		if (feriado.isEmpty()) {
			throw new RuntimeException(String.format("Nenhum feriado foi encontrado"));
		}
		return feriado.stream().map(mapper::fromFeriado).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "FIND_ALL_EQUIPAMENTOS")
	public List<EquipamentoResponse> findAllEquipamentos() {
		List<Equipamento> equipamento = equipamentoRepository.findAll();
		if (equipamento.isEmpty()) {
			throw new RuntimeException(String.format("Nenhum equipamento foi encontrado."));
		}
		return equipamento.stream().map(mapper::fromEquipamento).collect(Collectors.toList());
	}

	@LogAuditoria(acao = "CREATE_FERIADO_DATA")
	public Long createFeriadoData(FeriadoRequest request) {
		if (feriadoRepository.existsByNomeAndData(request.Nome(), request.Data())) {
			throw new RuntimeException(String.format("Feriado com nome e data semelhante ja existe:: %s, %s.",
					request.Nome(), request.Data()));
		}

		if (feriadoRepository.existsByNome(request.Nome())) {
			throw new RuntimeException(String.format("Feriado com nome semelhante:: %s", request.Nome()));
		}

		var feriado = feriadoRepository.save(mapper.toFeriado(request));
		return feriado.getId();
	}

	@LogAuditoria(acao = "CREATE_EQUIPAMENTO")
	public Long createEquipamento(EquipamentoRequest request) {
		if (equipamentoRepository.existsByNome(request.Nome())) {
			throw new RuntimeException(
					String.format("Equipamento com o mesmo nome ja existe:: %s.", request.Nome()));
		}
		var equipamento = equipamentoRepository.save(mapper.toEquipamento(request));
		return equipamento.getId();
	}

	@LogAuditoria(acao = "DELETE_EQUIPAMENTO")
	public void deleteEquipamento(Long id) {
		if (!equipamentoRepository.existsById(id)) {
			throw new RuntimeException(String.format("Equipamento com ID:: %s, não foi encontrado.", id));
		}
		equipamentoRepository.deleteById(id);
	}

	@LogAuditoria(acao = "DELETE_FERIADO")
	public void deleteFeriado(Long id) {
		if (!feriadoRepository.existsById(id)) {
			throw new RuntimeException(String.format("Feriado com ID:: %s, não foi encontrado.", id));
		}
		feriadoRepository.deleteById(id);
	}

}
