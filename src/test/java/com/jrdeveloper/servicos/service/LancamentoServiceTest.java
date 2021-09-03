package com.jrdeveloper.servicos.service;


import com.jrdeveloper.servicos.exceptions.RegraNegocioException;
import com.jrdeveloper.servicos.model.entity.Lancamento;
import com.jrdeveloper.servicos.model.enums.StatusLancamento;
import com.jrdeveloper.servicos.model.enums.TipoLancamento;
import com.jrdeveloper.servicos.model.repository.LancamentoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean //acessar os metodos originais da implementacao
    LancamentoServiceImpl service;

    @MockBean //simular o comportamente
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamentoASalvar = criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        Lancamento lancamento = service.salvar(lancamentoASalvar);

        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErrroDeValidacao(){
        /* CENARIO */
        Lancamento lancamentoASalvar = criarLancamento();
        // garantir que lance uma exceção
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

        /* EXECUCAO E VERIFICACAO */
        // certificar de que o tipo de erro é RegraNegocioException.class
        Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
        // certificar de que o método save nunca foi executado
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento(){
        /* CENARIO */
        Lancamento lancamentoSalvo = criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        /* EXECUÇÃO */
        // Garantir que não lance exceção
        Mockito.doNothing().when(service).validar(lancamentoSalvo);

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        /* VERIFICACAO */
        service.atualizar(lancamentoSalvo);
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);

    }


    @Test
    public void deveLancarErroAoTentarAtualizarLancamentoQueAindaNaoFoiSalvo(){
        /* CENARIO */
        Lancamento lancamentoASalvar = criarLancamento();

        /* EXECUCAO E VERIFICACAO */
        // certificar de que o tipo de erro é NullPointerException.class
        // deve ocorrer erro pq para atualizar, deve conter id, e na criação do objeto(criarLancamento()) não tem
        Assertions.catchThrowableOfType( () -> service.atualizar(lancamentoASalvar), NullPointerException.class);
        // certificar de que o método save nunca foi executado
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveDeletarUmLancamento(){
        // CENARIO
        Lancamento lancamento = criarLancamento();
        lancamento.setId(1l);

        // EXECUCAO
        service.deletar(lancamento);

        //VERIFICACAO
        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo(){
        Lancamento lancamento = criarLancamento();

        Assertions.catchThrowableOfType(()-> service.deletar(lancamento), NullPointerException.class);
        // certificar de que nunca chamou o metodo delete
        Mockito.verify(repository, Mockito.never()).delete(lancamento);
    }


    public Lancamento criarLancamento(){
        Lancamento lancamento = Lancamento.builder()
                .ano(2022)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
        return lancamento;
    }


}
