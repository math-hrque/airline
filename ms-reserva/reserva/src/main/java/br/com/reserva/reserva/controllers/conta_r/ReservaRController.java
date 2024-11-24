package br.com.reserva.reserva.controllers.conta_r;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.dtos.ReservaDto;
import br.com.reserva.reserva.dtos.VooDto;
import br.com.reserva.reserva.exeptions.ListaReservaVaziaException;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@RestController
@RequestMapping(value = "/ms-reserva")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaRController {

    @Autowired
    ReservaRService reservaRService;

    @GetMapping("/listar-reservas-voos-48h/{idCliente}")
    public ResponseEntity<?> listarReservasVoos48h(@PathVariable("idCliente") Long idCliente,
            @RequestBody List<VooDto> listaVooDto) {
        try {
            List<ReservaDto> listaVoos = reservaRService.listarReservasVoos48h(idCliente, listaVooDto);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaReservaVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultar-reserva/{codigoReserva}")
    public ResponseEntity<?> visualizarReservaCliente(@PathVariable String codigoReserva) {
        try {
            ReservaDto reservaDto = reservaRService.visualizarReservaCliente(codigoReserva);
            return ResponseEntity.status(HttpStatus.OK).body(reservaDto);
        } catch (ReservaNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar a reserva.");
        }
    }

    // @GetMapping("/consultar-reserva/{codigoReserva}")
    // public ResponseEntity<?> visualizarReservaCliente(@PathVariable String
    // codigoReserva) {
    // try {
    // ReservaDto reservaDto =
    // reservaRService.visualizarReservaCliente(codigoReserva);
    // return ResponseEntity.status(HttpStatus.OK).body(reservaDto);
    // } catch (ReservaNaoExisteException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao
    // buscar a reserva.");
    // }
    // }

    // @GetMapping("/cliente/{idCliente}")
    // public ResponseEntity<List<ReservaCUD>> getReservasByClienteId(@PathVariable
    // Long idCliente) {
    // Optional<List<ReservaCUD>> reservas =
    // reservaCUDRepository.findByIdCliente(idCliente);

    // if (reservas.isPresent() && !reservas.get().isEmpty()) {
    // return ResponseEntity.ok(reservas.get());
    // } else {
    // return ResponseEntity.noContent().build();
    // }
    // }
}
