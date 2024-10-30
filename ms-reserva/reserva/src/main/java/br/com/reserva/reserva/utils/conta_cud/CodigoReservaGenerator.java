package br.com.reserva.reserva.utils.conta_cud;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.reserva.reserva.repositories.conta_cud.ReservaCUDRepository;

@Component
public class CodigoReservaGenerator {

    @Autowired
    private ReservaCUDRepository reservaCUDRepository;

    public String gerarCodigoReservaUnico() {
        String codigo;
        boolean existe;
        do {
            codigo = gerarCodigoReserva();
            existe = reservaCUDRepository.existsByCodigoReserva(codigo);
        } while (existe);
        return codigo;
    }

    private String gerarCodigoReserva() {
        Random random = new Random();
        StringBuilder letras = new StringBuilder();
        StringBuilder numeros = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char letra = (char) (random.nextInt(26) + 'A');
            letras.append(letra);
        }
        for (int i = 0; i < 3; i++) {
            int numero = random.nextInt(10);
            numeros.append(numero);
        }
        return letras.toString() + numeros.toString();
    }
}
