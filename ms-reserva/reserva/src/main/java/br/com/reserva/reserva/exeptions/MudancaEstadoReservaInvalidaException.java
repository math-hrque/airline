package br.com.reserva.reserva.exeptions;

public class MudancaEstadoReservaInvalidaException extends Exception{
    public MudancaEstadoReservaInvalidaException(String mensagem){
        super(mensagem);
    }
}
