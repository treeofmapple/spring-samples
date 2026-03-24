package com.tom.management.mapper;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.tom.management.model.Usuario;
import com.tom.management.request.UsuarioRequest;
import com.tom.management.request.UsuarioResponse;

@Service
public class UsuarioMapper {

	public Usuario toUsuario(UsuarioRequest request) {
		if (request == null) {
			return null;
		}

		return Usuario.builder().nome(request.Nome()).email(request.Email()).perfis(request.Perfil() == null ? Set.of() : request.Perfil()).build();
	}

	public UsuarioResponse fromUsuario(Usuario usuario) {
		return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfis());
	}

}
