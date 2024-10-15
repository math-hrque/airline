package br.com.voos.voos.exeptions;

public class OutroVooDadosJaExistenteException extends Exception{
    public OutroVooDadosJaExistenteException(String mensagem){
        super(mensagem);
    }
}
