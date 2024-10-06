package br.com.funcionario.funcionario.exeptions;

public class FuncionarioNaoExisteException extends Exception{
    public FuncionarioNaoExisteException(String mensagem){
        super(mensagem);
    }
}
