package br.com.voos.voos.exeptions;

public class VooValidationException extends Exception{
    public VooValidationException(String mensagem){
        super(mensagem);
    }
}
