package com.jrdeveloper.servicos.model.repository;

import com.jrdeveloper.servicos.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
//@SpringBootTest  //Não utilizar em testes por simplificar os testes
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //evita alteração na configuração do banco de dados do arquivo application.properties
@ActiveProfiles("test") // automaticamente vai carregar o arquivo application-"test".properties
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        String email = "mail@mail.com";
        //cenário
        Usuario usuario = criarUsuario();
        //repository.save(usuario);
        entityManager.persist(usuario);

        //acão/Execução
        boolean result = repository.existsByEmail(email);

        //verificacao
        Assertions.assertThat(result).isTrue();
    };

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoPorEmail(){
        String search = "mail@mail.com";

        repository.deleteAll();

        boolean result = repository.existsByEmail(search);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        Usuario usuario = criarUsuario();

        Usuario usuarioSalvo = repository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){

        Usuario usuario = criarUsuario();

        entityManager.persist(usuario); //pelo entityManager, se a classe Usuario tiver id vai lançar uma exceção

        Optional<Usuario> result = repository.findByEmail("mail@mail.com");

        Assertions.assertThat(result.isPresent()).isTrue();

    }
    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExistirNaBase(){

        Optional<Usuario> result = repository.findByEmail("mail@mail.com");

        Assertions.assertThat(result.isPresent()).isFalse();

    }

    public static Usuario criarUsuario(){
        return Usuario.builder()
                .nome("augustus")
                .email("mail@mail.com")
                .senha("1234")
                .build();
    }

}
