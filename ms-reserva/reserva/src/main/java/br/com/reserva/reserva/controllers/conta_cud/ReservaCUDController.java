package br.com.reserva.reserva.controllers.conta_cud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;

@RestController
@RequestMapping(value = "/ms-reserva")
public class ReservaCUDController {

    @Autowired
    ReservaCUDService reservaCUDService;

    
}
