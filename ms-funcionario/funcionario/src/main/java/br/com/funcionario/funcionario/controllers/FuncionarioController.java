package br.com.funcionario.funcionario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.funcionario.funcionario.dtos.FuncionarioResponseDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.ListaFuncionarioVaziaException;
import br.com.funcionario.funcionario.services.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/funcionario")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

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
