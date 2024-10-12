package br.com.funcionario.funcionario.exeptions;

public class OutroFuncionarioDadosJaExistenteException extends Exception{
    public OutroFuncionarioDadosJaExistenteException(String mensagem){
        super(mensagem);
    }
}
