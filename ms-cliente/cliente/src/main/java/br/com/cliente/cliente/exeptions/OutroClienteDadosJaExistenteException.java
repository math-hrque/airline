package br.com.cliente.cliente.exeptions;

public class OutroClienteDadosJaExistenteException extends Exception{
    public OutroClienteDadosJaExistenteException(String mensagem){
        super(mensagem);
    }
}
