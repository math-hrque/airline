package br.com.funcionario.funcionario.exeptions;

public class OutroUsuarioDadosJaExistenteException extends Exception{
    public OutroUsuarioDadosJaExistenteException(String mensagem){
        super(mensagem);
    }
}
