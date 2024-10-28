package br.com.cliente.cliente.exeptions;

public class SemSaldoMilhasSuficientesClienteException extends Exception{
    public SemSaldoMilhasSuficientesClienteException(String mensagem){
        super(mensagem);
    }
}
