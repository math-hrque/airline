package br.com.auth.auth.exeptions;

public class UsuarioNaoExisteException extends Exception{
    public UsuarioNaoExisteException(String mensagem){
        super(mensagem);
    }
}
