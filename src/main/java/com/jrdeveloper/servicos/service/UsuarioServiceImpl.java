package com.jrdeveloper.servicos.service;

import com.jrdeveloper.servicos.exceptions.ErroAutenticacaoException;
import com.jrdeveloper.servicos.exceptions.RegraNegocioException;
import com.jrdeveloper.servicos.model.entity.Usuario;
import com.jrdeveloper.servicos.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if(!usuario.isPresent()){
            throw new ErroAutenticacaoException("Usuário não encontrado para o e-mail informado!");
        }
        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacaoException("Senha inválida!");
        }

        return usuario.get();
    }

    @Override
    @Transactional //abrir uma transação na base de dados e depois comitar
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Já existe um usuário cadastrado na base de dados!");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
}
