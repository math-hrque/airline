package br.com.voos.voos.exeptions;

public class AeroportoNaoExisteException extends Exception{
    public AeroportoNaoExisteException(String mensagem){
        super(mensagem);
    }
}
