package br.com.cliente.cliente.exeptions;

public class ClienteNaoExisteException extends Exception{
    public ClienteNaoExisteException(String mensagem){
        super(mensagem);
    }
}
