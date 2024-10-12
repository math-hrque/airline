package br.com.cliente.cliente.exeptions;

public class CepNaoExisteException extends Exception{
    public CepNaoExisteException(String mensagem){
        super(mensagem);
    }
}
