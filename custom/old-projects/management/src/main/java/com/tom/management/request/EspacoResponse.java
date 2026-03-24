package com.tom.management.request;

import java.util.Set;

import com.tom.management.model.Disponibilidade;
import com.tom.management.model.Equipamento;
import com.tom.management.request.dto.EquipamentoNomeDTO;

public record EspacoResponse(Long Id, String Nome, String Tipo, double Metragem, Disponibilidade Disponibilidade,
		EquipamentoNomeDTO Equipamentos) {

	public EspacoResponse(Long id, String nome, String tipo, double metragem, Disponibilidade disponibilidade, Set<Equipamento> equipamentos) {
	    this(id, nome, tipo, metragem, disponibilidade, EquipamentoNomeDTO.from(equipamentos));
	}
	
}
