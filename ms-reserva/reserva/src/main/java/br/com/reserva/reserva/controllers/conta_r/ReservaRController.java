package br.com.reserva.reserva.controllers.conta_r;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reserva.reserva.dtos.CodigoVooDto;
import br.com.reserva.reserva.dtos.ReservaDto;
import br.com.reserva.reserva.exeptions.ListaReservaVaziaException;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@RestController
@RequestMapping(value = "/ms-reserva")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaRController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaRController.class);

    @Autowired
    ReservaRService reservaRService;

    @GetMapping("/listar-reservas-voos-48h/{idCliente}")
    public ResponseEntity<?> listarReservasVoos48h(@PathVariable("idCliente") Long idCliente, @RequestBody List<CodigoVooDto> listaCodigoVoo) {
        try {
            List<ReservaDto> listaVoos = reservaRService.listarReservasVoos48h(idCliente, listaCodigoVoo);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaReservaVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar-reservas-voos-realizados-cancelados/{idCliente}")
    public ResponseEntity<?> listarReservasVoosRealizadosCancelados(@PathVariable("idCliente") Long idCliente, @RequestBody List<CodigoVooDto> listaCodigoVoo) {
        try {
            List<ReservaDto> listaVoos = reservaRService.listarReservasVoosRealizadosCancelados(idCliente, listaCodigoVoo);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaReservaVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/consultar-reserva/{codigoReserva}")
    public ResponseEntity<Object> visualizarReservaCliente(@PathVariable String codigoReserva) {
        try {
            ReservaDto reservaDto = reservaRService.visualizarReservaCliente(codigoReserva);
            return ResponseEntity.status(HttpStatus.OK).body(reservaDto);
        } catch (ReservaNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar a reserva.");
        }
    }

    @GetMapping("/listar-reservas-cliente/{idCliente}")
    public ResponseEntity<?> listarTodasReservasPorCliente(@PathVariable Long idCliente) {
        try {
            List<ReservaDto> reservas = reservaRService.listarTodasReservasPorCliente(idCliente);
            return ResponseEntity.ok(reservas);
        } catch (ListaReservaVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar reservas.");
        }
    }

    @GetMapping("/listar-reservas-confirmadas-cliente/{idCliente}")
    public ResponseEntity<?> listarTodasReservasConfirmadasPorCliente(@PathVariable Long idCliente) {
        try {
            List<ReservaDto> reservas = reservaRService.listarTodasReservasConfirmadasPorCliente(idCliente);
            return ResponseEntity.ok(reservas);
        } catch (ListaReservaVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar reservas.");
        }
    }
}
