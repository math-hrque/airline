package br.com.reserva.reserva.controllers.conta_r;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.services.conta_r.ReservaRService;

@RestController
@RequestMapping(value = "/ms-reserva")
public class ReservaRController {

    @Autowired
    ReservaRService reservaService;

  // código em construção!!
  // @GetMapping("/consultar-reserva/{id}")
  //   public ResponseEntity<Object> consultarReservaById(@PathVariable("id") String id) {
  //       try {
  //           Object reservaConsultada = reservaService.consultarReservaById(id);
  //           return ResponseEntity.status(HttpStatus.OK).body(reservaConsultada);
  //       } catch (ReservaNaoExisteException e) {
  //           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  //       } catch (Exception e) {
  //           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  //       }
  //   }
  
  // @GetMapping("/consultar-reserva/")
  //   public ResponseEntity<Object> consultarTodasReservas() {
  //       try {
  //           // lista todas as reservas
  //           Object reservaConsultada = reservaService.consultarReservas();
  //           return ResponseEntity.status(HttpStatus.OK).body(reservaConsultada);
  //       } catch (ReservaNaoExisteException e) {
  //           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  //       } catch (Exception e) {
  //           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  //       }
  //   }

  // // em construção
  // @PostMapping("/criar-reserva/")
  //   public ResponseEntity<Object> consultarTodasReservas(@) {
  //       try {
  //           // lista todas as reservas
  //           Object reservaConsultada = reservaService.consultarReservas();
  //           return ResponseEntity.status(HttpStatus.OK).body(reservaConsultada);
  //       } catch (ReservaNaoExisteException e) {
  //           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  //       } catch (Exception e) {
  //           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  //       }
  //   }

}
