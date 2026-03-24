package com.tom.management.request;

import java.util.Set;

import com.tom.management.model.Perfil;
import com.tom.management.model.Usuario;

public record AvaliadorResponse(Long Id, String Avaliador, Set<Perfil> Perfil) {
	public AvaliadorResponse(Long id, Usuario avaliador) {
		this(id, avaliador.getNome(), avaliador.getPerfis());
	}
}
