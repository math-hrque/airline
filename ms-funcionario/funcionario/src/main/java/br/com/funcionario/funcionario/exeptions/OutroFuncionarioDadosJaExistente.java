package br.com.funcionario.funcionario.exeptions;

public class OutroFuncionarioDadosJaExistente extends Exception{
    public OutroFuncionarioDadosJaExistente(String mensagem){
        super(mensagem);
    }
}
