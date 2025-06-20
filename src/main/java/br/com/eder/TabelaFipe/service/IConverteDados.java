package br.com.eder.TabelaFipe.service;

public interface IConverteDados {
    //retorna um tipo generico <T> tipo nao sei o que sera retornado
   <T> T obterDados(String json,Class <T> classe);

}
