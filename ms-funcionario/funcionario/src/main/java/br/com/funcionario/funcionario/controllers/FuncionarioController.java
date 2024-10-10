package br.com.funcionario.funcionario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.FuncionarioResponseDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.ListaFuncionarioVaziaException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistente;
import br.com.funcionario.funcionario.services.FuncionarioService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(value = "/api/funcionario")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    // @PostMapping("/cadastrar")
    // public ResponseEntity<Object> cadastrar(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
    //     try {
    //         Object funcionarioCriado = funcionarioService.cadastrar(funcionarioRequestDto);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioCriado);
    //     } catch (OutroFuncionarioDadosJaExistente e) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    //     }
    // }

    // @PutMapping("/atualizar")
    // public ResponseEntity<Object> atualizar(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
    //     try {
    //         Object funcionarioAtualizado = funcionarioService.atualizar(funcionarioRequestDto);
    //         return ResponseEntity.status(HttpStatus.OK).body(funcionarioAtualizado);
    //     } catch (FuncionarioNaoExisteException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    //     } catch (OutroFuncionarioDadosJaExistente e) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    //     }
    // }

    // @DeleteMapping("/inativar/{idFuncionario}")
    // public ResponseEntity<Object> inativar(@PathVariable("idFuncionario") Long idFuncionario) {
    //     try {
    //         Object funcionarioInativado = funcionarioService.inativar(idFuncionario);
    //         return ResponseEntity.status(HttpStatus.OK).body(funcionarioInativado);
    //     } catch (FuncionarioNaoExisteException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    //     }
    // }

    @GetMapping("/consultarIdFuncionario/{idFuncionario}")
    public ResponseEntity<Object> consultarId(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            Object funcionarioConsultado = funcionarioService.consultarId(idFuncionario);
            return ResponseEntity.status(HttpStatus.OK).body(funcionarioConsultado);
        } catch (FuncionarioNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultarEmail/{email}")
    public ResponseEntity<Object> consultarEmail(@PathVariable("email") String email) {
        try {
            Object funcionarioConsultado = funcionarioService.consultarEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(funcionarioConsultado);
        } catch (FuncionarioNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        try {
            List<FuncionarioResponseDto> listaFuncionario = funcionarioService.listar();
            return ResponseEntity.status(HttpStatus.OK).body(listaFuncionario);
        } catch (ListaFuncionarioVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
