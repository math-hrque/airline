package br.com.funcionario.funcionario.exeptions;

public class OutroUsuarioDadosJaExistente extends Exception{
    public OutroUsuarioDadosJaExistente(String mensagem){
        super(mensagem);
    }
}
