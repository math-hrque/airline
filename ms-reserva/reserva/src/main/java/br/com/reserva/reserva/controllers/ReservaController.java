package br.com.reserva.reserva.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.services.ReservaService;

@RestController
@RequestMapping(value = "/ms-reserva")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

}
