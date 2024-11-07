package br.com.voos.voos.exeptions;

public class VooJaExisteException extends Exception{
    public VooJaExisteException(String mensagem){
        super(mensagem);
    }
}
