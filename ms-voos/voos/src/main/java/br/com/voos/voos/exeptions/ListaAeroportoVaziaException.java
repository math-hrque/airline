package br.com.voos.voos.exeptions;

public class ListaAeroportoVaziaException extends Exception{
    public ListaAeroportoVaziaException(String mensagem){
        super(mensagem);
    }
}
