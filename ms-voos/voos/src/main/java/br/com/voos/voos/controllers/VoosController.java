package br.com.voos.voos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.voos.voos.dtos.VooDto;
import br.com.voos.voos.exeptions.ListaVoosVaziaException;
import br.com.voos.voos.services.VoosService;

import java.util.List;

@RestController
@RequestMapping(value = "/ms-voos")
public class VoosController {

    @Autowired
    VoosService vooService;

    @GetMapping("/listar-voos-48h")
    public ResponseEntity<?> listarVoos48h() {
        try {
            List<VooDto> listaVoos = vooService.listarVoos48h();
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
