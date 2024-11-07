package br.com.voos.voos.controllers;

import br.com.voos.voos.dtos.CadastrarVooDto;
import br.com.voos.voos.exeptions.AeroportoNaoExisteException;
import br.com.voos.voos.exeptions.VooJaExisteException;
import br.com.voos.voos.exeptions.VooValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.voos.voos.dtos.VooDto;
import br.com.voos.voos.exeptions.ListaVoosVaziaException;
import br.com.voos.voos.services.VoosService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ms-voos")
public class VoosController {

    @Autowired
    VoosService vooService;

    @PostMapping("/cadastrar-voo")
    public ResponseEntity<?> cadastrarVoo(@RequestBody CadastrarVooDto cadastrarVooDto) {
        try {
            VooDto vooCadastrado = vooService.cadastrarVoo(cadastrarVooDto);
            return ResponseEntity.status(HttpStatus.OK).body(vooCadastrado);
        }
        catch (VooValidationException | VooJaExisteException | AeroportoNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar-voos-48h")
    public ResponseEntity<?> listarVoos48h() {
        try {
            List<VooDto> listaVoos = vooService.listarVoosConfirmados48h();
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar-voos-atuais")
    public ResponseEntity<?> listarVoosAtuais(@RequestParam(required = false) Optional<String> codigoAeroportoOrigem, @RequestParam(required = false) Optional<String> codigoAeroportoDestino) {
        try {
            List<VooDto> listaVoos = vooService.listarVoosAtuais(codigoAeroportoOrigem, codigoAeroportoDestino);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
