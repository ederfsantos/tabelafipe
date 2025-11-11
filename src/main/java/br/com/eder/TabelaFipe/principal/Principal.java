package br.com.eder.TabelaFipe.principal;

import br.com.eder.TabelaFipe.model.Dados;
import br.com.eder.TabelaFipe.model.Modelos;
import br.com.eder.TabelaFipe.model.Veiculo;
import br.com.eder.TabelaFipe.service.ConsumoApi;
import br.com.eder.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    String opc;





        public void exibeMenu () {

            do {
                var menu = """
                        ***OPÇÕES***
                        Carro
                        Moto
                        Caminhão
                        Sair
                        
                        Digite uma das opções para consultar:
                        """;
                System.out.println(menu);
                var opcao = leitura.nextLine();
                String endereco;
                if (opcao.toLowerCase().contains("carr")) {
                    endereco = URL_BASE + "carros/marcas";
                } else if (opcao.toLowerCase().contains("mot")) {
                    endereco = URL_BASE + "motos/marcas";
                } else {
                    endereco = URL_BASE + "caminhoes/marcas";
                }


                var json = consumoApi.obterDados(endereco);//obtem uma string com os dados
                System.out.println(json);//exibe
                var marcas = conversor.obterLista(json, Dados.class);//transforma essa string em objeto tipo Dados

                marcas.stream()
                        .sorted(Comparator.comparing(Dados::codigo))
                        .forEach(System.out::println);

                System.out.println("Informe o codigo da marca para consulta: ");
                var codigoMarca = leitura.nextLine();

                endereco = endereco + "/" + codigoMarca + "/modelos";
                json = consumoApi.obterDados(endereco);
                var modeloLista = conversor.obterDados(json, Modelos.class);
                System.out.println("\nModelos dessa marca: ");
                modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo))
                        .forEach(System.out::println);

                System.out.println("\nDigite um trecho do nome do carro a ser buscado:");
                var nomeVeiculo = leitura.nextLine();
                List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                        .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                        .collect(Collectors.toList());
                System.out.println("\nModelos Filtrados");
                modelosFiltrados.forEach(System.out::println);

                System.out.println("\nDigite por favor o código do modelo para buscar os valores de avaliação: ");
                var codigoModelo = leitura.nextLine();

                endereco = endereco + "/" + codigoModelo + "/anos";
                json = consumoApi.obterDados(endereco);
                List<Dados> anos = conversor.obterLista(json, Dados.class);
                List<Veiculo> veiculos = new ArrayList<>();
                for (int i = 0; i < anos.size(); i++) {
                    var enderecoAnos = endereco + "/" + anos.get(i).codigo();
                    json = consumoApi.obterDados(enderecoAnos);
                    Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
                    veiculos.add(veiculo);
                }

                System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
                veiculos.forEach(System.out::println);


                System.out.println("Deseja realizar nova consulta? Sim ou Não: ");
                opc = leitura.nextLine();
            } while (opc.equalsIgnoreCase("Sim"));
            System.out.println("Até a próxima!");
        }
}
