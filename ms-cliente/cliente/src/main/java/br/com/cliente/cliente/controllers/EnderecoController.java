package br.com.cliente.cliente.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cliente.cliente.exeptions.CepInvalidoException;
import br.com.cliente.cliente.exeptions.CepNaoExisteException;
import br.com.cliente.cliente.services.EnderecoService;

@RestController
@RequestMapping(value = "/ms-cliente")
public class EnderecoController {

    @Autowired
    EnderecoService enderecoService;

    @GetMapping("/consultar-endereco/{cep}")
    public ResponseEntity<Object> consultar(@PathVariable("cep") String cep) {
        try {
            Object enderecoConsultado  = enderecoService.consultar(cep);
            return ResponseEntity.status(HttpStatus.OK).body(enderecoConsultado);
        } catch(CepNaoExisteException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch(CepInvalidoException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
