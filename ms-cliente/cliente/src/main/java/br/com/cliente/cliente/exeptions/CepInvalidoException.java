package br.com.cliente.cliente.exeptions;

public class CepInvalidoException extends Exception{
    public CepInvalidoException(String mensagem){
        super(mensagem);
    }
}
