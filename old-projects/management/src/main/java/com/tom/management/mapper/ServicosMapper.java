package com.tom.management.mapper;

import org.springframework.stereotype.Service;

import com.tom.management.model.Avaliador;
import com.tom.management.model.Equipamento;
import com.tom.management.model.Feriado;
import com.tom.management.request.AvaliadorResponse;
import com.tom.management.request.EquipamentoRequest;
import com.tom.management.request.EquipamentoResponse;
import com.tom.management.request.FeriadoRequest;
import com.tom.management.request.FeriadoResponse;

@Service
public class ServicosMapper {

	public Feriado toFeriado(FeriadoRequest request) {
		if (request == null) {
			return null;
		}

		return Feriado.builder().nome(request.Nome()).data(request.Data()).build();
	}

	public FeriadoResponse fromFeriado(Feriado feriado) {
		return new FeriadoResponse(feriado.getId(), feriado.getNome(), feriado.getData());
	}

	public Equipamento toEquipamento(EquipamentoRequest request) {
		if (request == null) {
			return null;
		}

		return Equipamento.builder().nome(request.Nome()).build();
	}

	public EquipamentoResponse fromEquipamento(Equipamento equipamento) {
		return new EquipamentoResponse(equipamento.getId(), equipamento.getNome());
	}
	
	public AvaliadorResponse fromAvaliador(Avaliador avaliador) {
		return new AvaliadorResponse(avaliador.getId(), avaliador.getAvaliador());
	}

}
