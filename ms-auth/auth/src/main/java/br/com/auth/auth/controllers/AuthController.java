package br.com.auth.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.auth.auth.dtos.Login;
import br.com.auth.auth.dtos.UsuarioRequestDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistente;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid Login login) {
        try {
            Object token = authService.login(login);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        try {
            Object token = authService.cadastrar(usuarioRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (OutroUsuarioDadosJaExistente e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{email}")
    public ResponseEntity<Object> atualizar(@PathVariable("email") String email, @RequestBody @Valid UsuarioRequestDto usuarioRequestDto) {
        try {
            Object usuarioAtualizado = authService.atualizar(email, usuarioRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
        } catch (UsuarioNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (OutroUsuarioDadosJaExistente e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/inativar/{email}")
    public ResponseEntity<Object> inativar(@PathVariable("email") String email) {
        try {
            Object usuarioInativado = authService.inativar(email);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioInativado);
        } catch (UsuarioNaoExisteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}