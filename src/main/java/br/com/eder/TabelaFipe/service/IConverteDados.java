package br.com.eder.TabelaFipe.service;

import java.util.List;

public interface IConverteDados {
    //retorna um tipo generico <T> tipo nao sei o que sera retornado
   <T> T obterDados(String json,Class <T> classe);

   <T> List<T> obterLista(String json, Class<T> classe);
}
