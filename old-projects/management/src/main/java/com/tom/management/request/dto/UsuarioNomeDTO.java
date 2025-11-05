package com.tom.management.request.dto;

import com.tom.management.model.Usuario;

public record UsuarioNomeDTO(String usuario) {

    public UsuarioNomeDTO(Usuario usuario) {
        this(usuario != null && usuario.getNome() != null
                ? usuario.getNome()
                : "System");
    }
}
