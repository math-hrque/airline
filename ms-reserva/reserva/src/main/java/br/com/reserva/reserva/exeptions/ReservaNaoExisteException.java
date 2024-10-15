package br.com.reserva.reserva.exeptions;

public class ReservaNaoExisteException extends Exception{
    public ReservaNaoExisteException(String mensagem){
        super(mensagem);
    }
}
