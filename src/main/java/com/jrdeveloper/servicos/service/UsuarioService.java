package com.jrdeveloper.servicos.service;

import com.jrdeveloper.servicos.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

}
