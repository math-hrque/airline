package br.com.voos.voos.exeptions;

public class VooNaoExisteException extends Exception{
    public VooNaoExisteException(String mensagem){
        super(mensagem);
    }
}
