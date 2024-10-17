package br.com.reserva.reserva.controllers.conta_r;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.services.conta_r.ReservaRService;

@RestController
@RequestMapping(value = "/ms-reserva")
public class ReservaRController {

    @Autowired
    ReservaRService reservaRService;


}
