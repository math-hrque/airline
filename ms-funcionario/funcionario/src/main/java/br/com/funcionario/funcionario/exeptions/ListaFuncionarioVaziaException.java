package br.com.funcionario.funcionario.exeptions;

public class ListaFuncionarioVaziaException extends Exception{
    public ListaFuncionarioVaziaException(String mensagem){
        super(mensagem);
    }
}
