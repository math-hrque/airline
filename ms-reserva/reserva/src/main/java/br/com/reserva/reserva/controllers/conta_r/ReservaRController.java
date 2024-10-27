package br.com.reserva.reserva.controllers.conta_r;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.dtos.ReservaDto;
import br.com.reserva.reserva.dtos.VooDto;
import br.com.reserva.reserva.exeptions.ListaReservaVaziaException;
import br.com.reserva.reserva.services.conta_r.ReservaRService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/ms-reserva")
public class ReservaRController {

    @Autowired
    ReservaRService reservaRService;

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


    @GetMapping("/listar-reservas-voos-48h/{idCliente}")
    public ResponseEntity<?> listarReservasVoos48h(@PathVariable("idCliente") Long idCliente, @RequestBody List<VooDto> listaVooDto) {
        try {
            List<ReservaDto> listaVoos = reservaRService.listarReservasVoos48h(idCliente, listaVooDto);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaReservaVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
