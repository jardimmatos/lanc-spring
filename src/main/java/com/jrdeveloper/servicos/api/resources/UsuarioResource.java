package com.jrdeveloper.servicos.api.resources;
import com.jrdeveloper.servicos.exceptions.ErroAutenticacaoException;
import com.jrdeveloper.servicos.exceptions.RegraNegocioException;
import com.jrdeveloper.servicos.model.entity.Usuario;
import com.jrdeveloper.servicos.service.LancamentoService;
import com.jrdeveloper.servicos.service.UsuarioService;
import com.jrdeveloper.servicos.api.dto.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private UsuarioService service;

    private LancamentoService lancamentoService;

    public UsuarioResource(UsuarioService service, LancamentoService lancamentoService) {
        this.service = service;
        this.lancamentoService = lancamentoService;
    }

    @GetMapping("/hello-world")
    public String helloWorld(){
        return "Hello, World!";
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario.builder()
                            .nome(dto.getNome())
                            .email(dto.getEmail())
                            .senha(dto.getSenha())
                            .build();
        try{
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
        try{
            Usuario usuatioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuatioAutenticado);
        }catch(ErroAutenticacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id){
        //Verificar se usuário existe, primeiro
        Optional<Usuario> usuario = service.obterPorId(id);
        if(!usuario.isPresent())
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);

        BigDecimal saldo = lancamentoService.obterSaldoUsuario(id);
        return ResponseEntity.ok(saldo);
    }


}
