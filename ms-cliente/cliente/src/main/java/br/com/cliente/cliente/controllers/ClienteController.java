package br.com.cliente.cliente.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cliente.cliente.dtos.ClienteDto;
import br.com.cliente.cliente.dtos.ClienteMilhasDto;
import br.com.cliente.cliente.dtos.SaldoMilhasDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.services.ClienteService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/ms-cliente")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    ClienteService clienteService;

    @PostMapping("/comprar-milhas")
    public ResponseEntity<Object> comprarMilhas(@RequestParam("idCliente") Long idCliente, @RequestParam("quantidadeMilhas") int quantidadeMilhas) {
        try {
            SaldoMilhasDto novaSaldo = clienteService.comprarMilhas(quantidadeMilhas, idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaSaldo);
        } catch (ClienteNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar-milhas/{idCliente}")
    public ResponseEntity<Object> consultarExtratoMilhas(@PathVariable("idCliente") Long idCliente) {
        try {
            ClienteMilhasDto extrato = clienteService.consultarExtratoMilhas(idCliente);
            return ResponseEntity.ok(extrato);
        } catch (ClienteNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/consultar-email/{email}")
    public ResponseEntity<Object> consultarEmail(@PathVariable("email") String email) {
        try {
            ClienteDto clienteConsultado = clienteService.consultarEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(clienteConsultado);
        } catch (ClienteNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultar-idcliente/{idCliente}")
    public ResponseEntity<Object> consultarIdCliente(@PathVariable("idCliente") Long idCliente) {
        try {
            ClienteDto clienteConsultado = clienteService.consultarIdCliente(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(clienteConsultado);
        } catch (ClienteNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
