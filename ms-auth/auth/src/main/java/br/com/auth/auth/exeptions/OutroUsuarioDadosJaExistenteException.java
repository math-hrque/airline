package br.com.auth.auth.exeptions;

public class OutroUsuarioDadosJaExistenteException extends Exception{
    public OutroUsuarioDadosJaExistenteException(String mensagem){
        super(mensagem);
    }
}
