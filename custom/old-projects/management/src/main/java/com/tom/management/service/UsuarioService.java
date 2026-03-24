package com.tom.management.service;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.management.config.LogAuditoria;
import com.tom.management.mapper.UsuarioMapper;
import com.tom.management.model.Perfil;
import com.tom.management.model.Usuario;
import com.tom.management.repository.UsuarioRepository;
import com.tom.management.request.UsuarioRequest;
import com.tom.management.request.UsuarioResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository repository;
	private final UsuarioMapper mapper;

	@LogAuditoria(acao = "FIND_ALL_USUARIO")
	public List<UsuarioResponse> findAll() {
		List<Usuario> usuarios = repository.findAll();
		if (usuarios.isEmpty()) {
			throw new RuntimeException(String.format("Nenhum usuario encontrado."));
		}
		return usuarios.stream().map(mapper::fromUsuario).collect(Collectors.toList());
	}
	
	@LogAuditoria(acao = "FIND_BY_ID_USUARIO")
	public UsuarioResponse findById(Long usuarioId) {
		return repository.findById(usuarioId).map(mapper::fromUsuario).orElseThrow(
				() -> new RuntimeException(String.format("Usuario com id: %s, não foi encontrado", usuarioId)));
	}

	@LogAuditoria(acao = "CREATE_USUARIO")
	public Long createUsuario(UsuarioRequest request) {
	    if (repository.existsByEmail(request.Email())) {
	        throw new RuntimeException("Usuário com mesmo email já existe: %s" + request.Email());
	    }
	    
	    var perfis = request.Perfil();
	    if (perfis.size() != new HashSet<>(perfis).size()) {
	        throw new RuntimeException("Perfis duplicados não são permitidos: %s" + perfis);
	    }

	    var perfisPermitidos = EnumSet.allOf(Perfil.class);
	    if (request.Perfil().stream().noneMatch(perfisPermitidos::contains)) {
	        throw new RuntimeException("Perfil não permitido: %s" + request.Perfil());
	    }

	    return repository.save(mapper.toUsuario(request)).getId();
	}

	@LogAuditoria(acao = "UPDATE_USUARIO")
	public void updateUsuario(Long usuarioId, UsuarioRequest request) {
		var usuario = repository.findById(usuarioId).orElseThrow(() -> new RuntimeException(String
				.format("Não foi possivel atualizar o Usuario, não foi encontrado Usuario com id:: %s", usuarioId)));

		mergerUsuario(usuario, request);
		repository.save(usuario);
	}

	@LogAuditoria(acao = "DELETE_USUARIO")
	public void deleteUsuario(Long usuarioId) {
		if (!repository.existsById(usuarioId)) {
			throw new RuntimeException("Usuario com ID:: %s, não foi encontrado" + usuarioId);
		}
		repository.deleteById(usuarioId);
	}

	private void mergerUsuario(Usuario usuario, UsuarioRequest request) {
		usuario.setNome(request.Nome());
		usuario.setEmail(request.Email());
		usuario.setPerfis(request.Perfil() == null ? Set.of() : request.Perfil());
	}
}
