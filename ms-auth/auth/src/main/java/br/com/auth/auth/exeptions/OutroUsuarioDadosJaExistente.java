package br.com.auth.auth.exeptions;

public class OutroUsuarioDadosJaExistente extends Exception{
    public OutroUsuarioDadosJaExistente(String mensagem){
        super(mensagem);
    }
}
