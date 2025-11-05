package com.tom.management.request;

import java.util.Set;

import com.tom.management.model.Perfil;

public record UsuarioResponse(Long Id, String Nome, String Email, Set<Perfil> Perfil) {

}
