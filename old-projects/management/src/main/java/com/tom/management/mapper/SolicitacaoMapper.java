package com.tom.management.mapper;

import org.springframework.stereotype.Service;

import com.tom.management.model.EspacoFisico;
import com.tom.management.model.Solicitacao;
import com.tom.management.model.Status;
import com.tom.management.model.Usuario;
import com.tom.management.request.SolicitacaoRequest;
import com.tom.management.request.SolicitacaoResponse;

@Service
public class SolicitacaoMapper {

	public Solicitacao toSolicitacoes(SolicitacaoRequest request) {
		if (request == null) {
			return null;
		}

		var espaco = EspacoFisico.builder().id(request.EspacoId()).build();
		var usuario = Usuario.builder().id(request.UsuarioId()).build();

		return Solicitacao.builder().espaco(espaco).solicitante(usuario).nome(request.Nome())
				.dataInicio(request.DataInicio()).dataFim(request.DataFim()).horaInicio(request.HoraInicio())
				.horaFim(request.HoraFim()).status(Status.PENDENTE).justificativa(request.Justificativa()).build();
	}

	public SolicitacaoResponse fromSolicitacoes(Solicitacao sol) {
		return new SolicitacaoResponse(sol.getId(), sol.getEspaco(), sol.getSolicitante(), sol.getNome(),
				sol.getDataInicio(), sol.getDataFim(), sol.getHoraInicio(), sol.getHoraFim(), sol.getDataSolicitacao(),
				sol.getDataStatus(), sol.getStatus(), sol.getJustificativa());
	}

}
