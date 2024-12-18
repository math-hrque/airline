package br.com.funcionario.funcionario.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(value = "/ms-funcionario")
@CrossOrigin(origins = "http://localhost:4200")
public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);

    @Autowired
    FuncionarioService funcionarioService;

    @GetMapping("/listar-funcionario")
    public ResponseEntity<?> listar() {
        try {
            List<FuncionarioResponseDto> listaFuncionario = funcionarioService.listar();
            return ResponseEntity.status(HttpStatus.OK).body(listaFuncionario);
        } catch (ListaFuncionarioVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultar-email/{email}")
    public ResponseEntity<Object> consultarEmail(@PathVariable("email") String email) {
        try {
            FuncionarioResponseDto funcionarioConsultado = funcionarioService.consultarEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(funcionarioConsultado);
        } catch (FuncionarioNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultar-idfuncionario/{idFuncionario}")
    public ResponseEntity<Object> consultarIdFuncionario(@PathVariable("idFuncionario") Long idFuncionario) {
        try {
            FuncionarioResponseDto funcionarioConsultado = funcionarioService.consultarIdFuncionario(idFuncionario);
            return ResponseEntity.status(HttpStatus.OK).body(funcionarioConsultado);
        } catch (FuncionarioNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
