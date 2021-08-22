package com.jrdeveloper.servicos.api.resources;

import com.jrdeveloper.servicos.api.dto.AtualizaStatusDTO;
import com.jrdeveloper.servicos.api.dto.LancamentoDTO;
import com.jrdeveloper.servicos.exceptions.RegraNegocioException;
import com.jrdeveloper.servicos.model.entity.Lancamento;
import com.jrdeveloper.servicos.model.entity.Usuario;
import com.jrdeveloper.servicos.model.enums.StatusLancamento;
import com.jrdeveloper.servicos.model.enums.TipoLancamento;
import com.jrdeveloper.servicos.service.LancamentoService;
import com.jrdeveloper.servicos.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

    private LancamentoService service;
    private UsuarioService usuarioService;

    public LancamentoResource(LancamentoService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value="descricao",required = false) String descricao,
            @RequestParam(value="mes",required = false) Integer mes,
            @RequestParam(value="ano",required = false) Integer ano,
            @RequestParam(value="tipo",required = false) String tipo,
            @RequestParam(value="status",required = false) String status,
            @RequestParam("usuario") Long idUsuario
            ){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        if(tipo != null)
            lancamentoFiltro.setTipo(TipoLancamento.valueOf(tipo));
        if(status != null)
            lancamentoFiltro.setStatus(StatusLancamento.valueOf(status));
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if (!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não informado/encontrado!");
        }else{
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> lancamentos = service.listar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
        try{
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
        return service.obterPorId(id).map(entity->{
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch(Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( ()->
                new ResponseEntity("Lançamento não encontrado na base de dados!",HttpStatus.BAD_REQUEST)
        );
    }

    @PutMapping("/{id}/atualizar-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto){
        return service.obterPorId(id).map(entity->{
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null){
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido");
            }
            try{
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch(RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(()-> new ResponseEntity("Lançamento",HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.obterPorId(id).map(entity -> {
            service.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(()-> new ResponseEntity("Lançamento não encontrado para exclusão!", HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter(LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
                .orElseThrow(()-> new RegraNegocioException("Usuário não encontrado para o ID informado!"));
        lancamento.setUsuario(usuario);
        if(dto.getTipo() != null)
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        if(dto.getStatus() != null)
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        return lancamento;
    }

}
