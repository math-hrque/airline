package br.com.voos.voos.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.voos.voos.dtos.CodigoAeroportoDto;
import br.com.voos.voos.exeptions.ListaAeroportoVaziaException;
import br.com.voos.voos.services.AeroportoService;

@RestController
@RequestMapping(value = "/ms-voos")
@CrossOrigin(origins = "http://localhost:4200")
public class AeroportoController {

    private static final Logger logger = LoggerFactory.getLogger(AeroportoController.class);

    @Autowired
    AeroportoService aeroportoService;

    @GetMapping("/listar-aeroporto")
    public ResponseEntity<?> listar() {
        try {
            List<CodigoAeroportoDto> listaAeroporto = aeroportoService.listarAeroportos();
            return ResponseEntity.status(HttpStatus.OK).body(listaAeroporto);
        } catch (ListaAeroportoVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
