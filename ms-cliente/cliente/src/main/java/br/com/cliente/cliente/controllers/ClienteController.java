package br.com.cliente.cliente.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cliente.cliente.services.ClienteService;

@RestController
@RequestMapping(value = "/ms-cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

}
