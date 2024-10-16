package br.com.voos.voos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.voos.voos.dtos.CodigoAeroportoDto;
import br.com.voos.voos.exeptions.ListaAeroportoVaziaException;
import br.com.voos.voos.services.AeroportoService;

@RestController
@RequestMapping(value = "/ms-voos")
public class AeroportoController {

    @Autowired
    AeroportoService aeroportoService;

    @GetMapping("/listar-aeroporto")
    public ResponseEntity<?> listar() {
        try {
            List<CodigoAeroportoDto> listaAeroporto = aeroportoService.listar();
            return ResponseEntity.status(HttpStatus.OK).body(listaAeroporto);
        } catch (ListaAeroportoVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
