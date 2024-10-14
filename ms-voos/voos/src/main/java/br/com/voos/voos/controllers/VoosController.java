package br.com.voos.voos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.voos.voos.services.VoosService;

@RestController
@RequestMapping(value = "/ms-voos")
public class VoosController {

    @Autowired
    VoosService vooService;

}
