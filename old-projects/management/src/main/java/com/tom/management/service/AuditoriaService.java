package com.tom.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.management.config.LogAuditoria;
import com.tom.management.mapper.AuditoriaMapper;
import com.tom.management.model.Auditoria;
import com.tom.management.repository.AuditoriaRepository;
import com.tom.management.repository.UsuarioRepository;
import com.tom.management.request.AuditoriaRequest;
import com.tom.management.request.AuditoriaResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaService {

	private final UsuarioRepository usuarioRepository;
	private final AuditoriaRepository auditoriaRepository;
	private final AuditoriaMapper mapper;

	public List<AuditoriaResponse> findAll() {
		List<Auditoria> auditoria = auditoriaRepository.findAll();
		if (auditoria.isEmpty()) {
			throw new RuntimeException(String.format("Nenhuma auditoria foi encontrada"));
		}
		return auditoria.stream().map(mapper::fromAuditoria).collect(Collectors.toList());
	}

	public AuditoriaResponse findById(Long auditoriaId) {
		return auditoriaRepository.findById(auditoriaId).map(mapper::fromAuditoria).orElseThrow(
				() -> new RuntimeException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId)));
	}

	public Long createAuditoria(AuditoriaRequest request) {
		var usuarioId = usuarioRepository.findById(request.UsuarioId()).orElseThrow(
				() -> new RuntimeException(String.format("Usuario com id: %s não existe", request.UsuarioId())));

		LocalDateTime windowStart = request.Data().minusSeconds(10);
		LocalDateTime windowEnd = request.Data().plusSeconds(10);

		if (auditoriaRepository.existsByUsuarioAndAcaoAndDataBetween(usuarioId, request.Acao(), windowStart,
				windowEnd)) {
			throw new RuntimeException(String
					.format("Evento de auditoria similar foi detectado em um periodo curto de tempo", request.Data()));
		}
		
		var auditoria = auditoriaRepository.save(mapper.toAuditoria(request));
		return auditoria.getId();
	}

	public void updateAuditoria(Long auditoriaId, AuditoriaRequest request) {
		var auditoria = auditoriaRepository.findById(auditoriaId).orElseThrow(
				() -> new RuntimeException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId)));

		mergerAuditoria(auditoria, request);
		auditoriaRepository.save(auditoria);
	}

	@LogAuditoria(acao = "DELETE_AUDITORIA")
	public void deleteAuditoria(Long auditoriaId) {
		if (!auditoriaRepository.existsById(auditoriaId)) {
			throw new RuntimeException(String.format("Auditoria com id: %s não foi encontrado", auditoriaId));
		}
		auditoriaRepository.deleteById(auditoriaId);
	}

	private void mergerAuditoria(Auditoria auditoria, AuditoriaRequest request) {
		var mapping = mapper.toAuditoria(request);
		auditoria.setUsuario(mapping.getUsuario());
		auditoria.setAcao(mapping.getAcao());
		auditoria.setData(mapping.getData());
		auditoria.setDetalhes(mapping.getDetalhes());
	}

}
