package br.com.reserva.reserva.exeptions;

public class ListaReservaVaziaException extends Exception{
    public ListaReservaVaziaException(String mensagem){
        super(mensagem);
    }
}
