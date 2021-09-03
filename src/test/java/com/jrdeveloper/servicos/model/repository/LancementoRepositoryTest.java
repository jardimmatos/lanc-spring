package com.jrdeveloper.servicos.model.repository;

import com.jrdeveloper.servicos.model.entity.Lancamento;
import com.jrdeveloper.servicos.model.enums.StatusLancamento;
import com.jrdeveloper.servicos.model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
//@SpringBootTest  //Não utilizar em testes por simplificar os testes
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //evita alteração na configuração do banco de dados do arquivo application.properties
@ActiveProfiles("test") // automaticamente vai carregar o arquivo application-"test".properties
public class LancementoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamento = Lancamento.builder()
                .ano(2021)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();

        lancamento = repository.save(lancamento);
        Assertions.assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void devDeletarUmLancamento(){
        Lancamento lancamento = Lancamento.builder()
                .ano(2022)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
        lancamento = entityManager.persist(lancamento);
        entityManager.find(Lancamento.class, lancamento.getId());
        repository.delete((lancamento));
        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
        Assertions.assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamentoNaBase(){
        Lancamento lancamento = Lancamento.builder()
                .ano(2022)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
        entityManager.persist(lancamento);
        lancamento.setAno(2023);
        lancamento.setDescricao("Atualizando");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        repository.save(lancamento);
        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2023);
        Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Atualizando");
        Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId(){
        Lancamento lancamento = Lancamento.builder()
                .ano(2022)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
        entityManager.persist(lancamento);

        Optional<Lancamento> lancamentoSalvo = repository.findById(lancamento.getId());
        Assertions.assertThat(lancamentoSalvo.isPresent()).isTrue();


    }

 }
