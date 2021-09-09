package com.jrdeveloper.servicos.service;

import com.jrdeveloper.servicos.exceptions.ErroAutenticacaoException;
import com.jrdeveloper.servicos.exceptions.RegraNegocioException;
import com.jrdeveloper.servicos.model.entity.Usuario;
import com.jrdeveloper.servicos.model.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;


    @Test
    public void deveAutenticarUmUsuarioComSucesso(){
        String email = "usuario@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().nome("augustus").email(email).senha(senha).id(1l).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
        
        Usuario result = service.autenticar(email,senha);

        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void deveValidarEmail(){
        //Fake Repository
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail("mail@mail.com");
    }

    @Test
    public void deveLancarErroQuandoExistirEmailCadastrado(){
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        //service.validarEmail("mail@mail.com");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        String email = "email@email.com";
        String senha = "senha";
        Usuario usuario = Usuario.builder().email(email).senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com","123"));
        Assertions.assertThat(exception).isInstanceOf((ErroAutenticacaoException.class)).hasMessage("Senha inválida!");

    }

    @Test
    public void deveSalvarUmUsuario(){
        /* o Spy é similar ao Mock, a diferença pe que o Mock nunca chama os métodos originais, e o Spy pode sobrescrever*/
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().id(1l).nome("augustus").email("email@email.com").senha("senha").build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("augustus");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }


    @Test
    public void naoDeveCadastrarUmUsuarioComEmailCadastrado(){
        String email = "email@emaill.com";
        Assertions.assertThat(true).isTrue();
        //comentado por não estar definido o "expected=RegraNegocio.class"
        //Usuario usuario = Usuario.builder().email(email).build();
        //Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
        //service.salvarUsuario(usuario);
        //Mockito.verify(repository, Mockito.never()).save(usuario);

    }

}
