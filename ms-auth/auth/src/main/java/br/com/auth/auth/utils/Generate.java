package br.com.auth.auth.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Generate {

    private static final String caracteres = "0123456789";

    public static String generatePassword() {
        int tamanho = 4;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(tamanho);

        for (int i = 0; i < tamanho; i++) {
            int randomIndex = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(randomIndex));
        }
        return password.toString();
    }
}
