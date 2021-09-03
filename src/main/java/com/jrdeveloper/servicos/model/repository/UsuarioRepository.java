package com.jrdeveloper.servicos.model.repository;

import com.jrdeveloper.servicos.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Query methods
    Optional<Usuario> findByEmail(String email);
    //Optional<Usuario> findByEmailAndNome(String email, String nome);
    boolean existsByEmail(String email);

}
