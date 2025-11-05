package com.tom.management.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.tom.management.model.Equipamento;
import com.tom.management.model.EspacoFisico;
import com.tom.management.model.Status;
import com.tom.management.model.Usuario;

public record SolicitacaoResponse(Long Id, String NomeEspaco, String TipoEspaco, Set<String> Equipamentos,
		String Usuario, String Solicitacao, LocalDate DataInicio, LocalDate DataFim, LocalTime HoraInicio,
		LocalTime HoraFim, LocalDate DataSolicitacao, LocalDate DataStatus, Status Status, String Justificativa) {

	public SolicitacaoResponse(Long Id, EspacoFisico Espaco, Usuario Usuario, String Solicitacao, LocalDate DataInicio,
			LocalDate DataFim, LocalTime HoraInicio, LocalTime HoraFim, LocalDate DataSolicitacao, LocalDate DataStatus,
			Status Status, String Justificativa) {
		this(Id, Espaco.getNome(), Espaco.getTipo(),
				Espaco.getEquipamentos().stream().map(Equipamento::getNome).collect(Collectors.toSet()),
				Usuario.getNome().toString(), Solicitacao, DataInicio, DataFim, HoraInicio, HoraFim, DataSolicitacao,
				DataStatus, Status, Justificativa);
	}

}
