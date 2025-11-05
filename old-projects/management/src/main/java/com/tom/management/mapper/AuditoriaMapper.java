package com.tom.management.mapper;

import org.springframework.stereotype.Service;

import com.tom.management.model.Auditoria;
import com.tom.management.model.Usuario;
import com.tom.management.request.AuditoriaRequest;
import com.tom.management.request.AuditoriaResponse;

@Service
public class AuditoriaMapper {

	public Auditoria toAuditoria(AuditoriaRequest request) {
		if (request == null) {
			return null;
		}

		var usuario = Usuario.builder().id(request.UsuarioId()).build();

		return Auditoria.builder().usuario(usuario).acao(request.Acao()).data(request.Data())
				.detalhes(request.Detalhes()).build();
	}

	public AuditoriaResponse fromAuditoria(Auditoria auditoria) {
		return new AuditoriaResponse(auditoria.getId(), auditoria.getUsuario(), auditoria.getAcao(),
				auditoria.getData(), auditoria.getDetalhes());
	}

}
