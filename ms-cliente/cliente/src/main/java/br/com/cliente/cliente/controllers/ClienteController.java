package br.com.cliente.cliente.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cliente.cliente.dtos.ClienteMilhasDto;
import br.com.cliente.cliente.dtos.MilhasDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.services.ClienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/ms-cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/listar-milhas/{idCliente}")
    public ResponseEntity<ClienteMilhasDto> consultarExtratoMilhas(@PathVariable("idCliente") Long idCliente) {
        try {
            ClienteMilhasDto extrato = clienteService.consultarExtratoMilhas(idCliente);
            return ResponseEntity.ok(extrato);
        } catch (ClienteNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/comprar-milhas")
    public ResponseEntity<MilhasDto> comprarMilhas(@RequestParam("idCliente") Long idCliente,
            @RequestParam("quantidadeMilhas") int quantidadeMilhas, @RequestBody MilhasDto milhas) {
        milhas.setQuantidadesMilhas(quantidadeMilhas);
        try {
            clienteService.comprarMilhas(milhas, idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(milhas);
        } catch (ClienteNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // @GetMapping("/listar-milhas/{idCliente}")
    // public ResponseEntity<ClienteMilhasDto>
    // consultarExtratoMilhas(@PathVariable("idCliente") Long idCliente) {
    // try {
    // ClienteMilhasDto extrato = clienteService.consultarExtratoMilhas(idCliente);
    // return ResponseEntity.ok(extrato);
    // } catch (ClienteNaoExisteException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    // }
    // }

}
