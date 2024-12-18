package br.com.voos.voos.controllers;

import br.com.voos.voos.dtos.CadastrarVooDto;
import br.com.voos.voos.dtos.CodigoVooDto;
import br.com.voos.voos.exeptions.AeroportoNaoExisteException;
import br.com.voos.voos.exeptions.VooJaExisteException;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.exeptions.VooValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(origins = "http://localhost:4200")
public class VoosController {

    private static final Logger logger = LoggerFactory.getLogger(VoosController.class);

    @Autowired
    VoosService vooService;

    @PostMapping("/cadastrar-voo")
    public ResponseEntity<Object> cadastrarVoo(@RequestBody CadastrarVooDto cadastrarVooDto) {
        try {
            VooDto vooCadastrado = vooService.cadastrarVoo(cadastrarVooDto);
            return ResponseEntity.status(HttpStatus.OK).body(vooCadastrado);
        } catch (VooValidationException | VooJaExisteException | AeroportoNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar-voos-48h")
    public ResponseEntity<?> listarVoosConfirmados48h() {
        try {
            List<VooDto> listaVoos = vooService.listarVoosConfirmados48h();
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listar-voos-atuais")
    public ResponseEntity<?> listarVoosAtuais(@RequestParam(required = false) Optional<String> codigoAeroportoOrigem, @RequestParam(required = false) Optional<String> codigoAeroportoDestino) {
        try {
            List<VooDto> listaVoos = vooService.listarVoosAtuais(codigoAeroportoOrigem, codigoAeroportoDestino);
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/visualizar-voo/{codigoVoo}")
    public ResponseEntity<Object> visualizarVoo(@PathVariable String codigoVoo) {
        try {
            VooDto vooDto = vooService.visualizarVoo(codigoVoo);
            return ResponseEntity.status(HttpStatus.OK).body(vooDto);
        } catch (VooNaoExisteException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar o voo.");
        }
    }

    @GetMapping("/listar-voos-codigos")
    public ResponseEntity<?> listarVoosPorCodigos(@RequestBody List<CodigoVooDto> listaCodigoVoo) {
        try {
            List<VooDto> vooDto = vooService.listarVoosPorCodigos(listaCodigoVoo);
            return ResponseEntity.status(HttpStatus.OK).body(vooDto);
        } catch (ListaVoosVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar o voos.");
        }
    }

    @GetMapping("/listar-voos-realizados-cancelados")
    public ResponseEntity<?> listarVoosRealizadosCancelados() {
        try {
            List<VooDto> listaVoos = vooService.listarVoosRealizadosCancelados();
            return ResponseEntity.status(HttpStatus.OK).body(listaVoos);
        } catch (ListaVoosVaziaException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
