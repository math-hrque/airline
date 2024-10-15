package br.com.voos.voos.exeptions;

public class ListaVoosVaziaException extends Exception{
    public ListaVoosVaziaException(String mensagem){
        super(mensagem);
    }
}
