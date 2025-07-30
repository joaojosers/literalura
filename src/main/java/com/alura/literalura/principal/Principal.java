package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConverteDados;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    // ✅ URL corrigida
    private final String URL_BASE = "https://gutendex.com/books/";

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    
                    Escolha o número de sua opção:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Gerar estatísticas de downloads
                    7 - Listar Top 10 livros mais baixados
                    8 - Buscar autor por nome
                    9 - Listar autores por ano de nascimento
                    10 - Listar autores por ano de falecimento
                    0 - Sair
                    """;
            System.out.println(menu);
            System.out.print("Digite a opção desejada: ");
            String entrada = leitura.nextLine();

            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrada inválida. Digite um número válido.");
                opcao = -1; // repete o menu
            }

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosNoAno();
                case 5 -> listarLivrosPorIdioma();
                case 6 -> gerarEstatisticasDownloads();
                case 7 -> listarTop10Livros();
                case 8 -> buscarAutorPorNome();
                case 9 -> listarAutoresPorAnoNascimento();
                case 10 -> listarAutoresPorAnoFalecimento();
                case 0 -> System.out.println("Saindo...");
                default -> {
                    if (opcao != -1) {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                }
            }
        }
    }

    // ================== Buscar Livro ==================
    private void buscarLivroPorTitulo() {
        System.out.println("Digite o nome do livro que deseja buscar:");
        var nomeLivro = leitura.nextLine().trim();

        // URL final corrigida
        String url = URL_BASE + "?search=" + nomeLivro.replace(" ", "+");
        System.out.println("🔎 Buscando livro na API: " + url);

        var json = consumo.obterDados(url);

        if (json == null) {
            System.out.println("⚠️ Não foi possível buscar o livro. Verifique a conexão ou a URL.");
            return;
        }

        var resultadoBusca = conversor.obterDados(json, ResultadoBusca.class);

        if (resultadoBusca == null || resultadoBusca.resultados() == null || resultadoBusca.resultados().isEmpty()) {
            System.out.println("⚠️ Nenhum resultado encontrado para o título informado.");
            return;
        }

        // ✅ Filtrar resultados com título exatamente igual (ignorando maiúsculas/minúsculas)
        var livroEncontrado = resultadoBusca.resultados()
                .stream()
                .filter(l -> l.titulo().equalsIgnoreCase(nomeLivro))
                .findFirst();

        if (livroEncontrado.isEmpty()) {
            System.out.println("⚠️ Nenhum livro encontrado com título EXATO: " + nomeLivro);
            return;
        }

        DadosLivro livroDados = livroEncontrado.get();
        System.out.println("📚 Livro encontrado: " + livroDados.titulo());

        // Salvar autor
        Autor autor = null;
        if (!livroDados.autores().isEmpty()) {
            var dadosAutor = livroDados.autores().get(0);
            autor = autorRepository.findByNomeContainingIgnoreCase(dadosAutor.nome())
                    .orElseGet(() -> autorRepository.save(new Autor(dadosAutor)));
        }

        // ✅ Verificar se o livro já existe no banco antes de salvar
        boolean existe = livroRepository.findByTituloContainingIgnoreCase(livroDados.titulo()).isPresent();
        if (!existe) {
            Livro livro = new Livro(livroDados);
            livro.setAutor(autor);
            livroRepository.save(livro);
            System.out.println("✅ Livro salvo no banco de dados!");
        } else {
            System.out.println("ℹ️ O livro já existe no banco de dados.");
        }
    }

    // ================== Listagens ==================
    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosNoAno() {
        System.out.println("Digite o ano que deseja pesquisar:");
        String entrada = leitura.nextLine();

        try {
            int ano = Integer.parseInt(entrada);
            List<Autor> autoresVivos = autorRepository.findAutoresVivosNoAno(ano);
            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor encontrado vivo no ano " + ano);
            } else {
                System.out.println("Autores vivos no ano " + ano + ":");
                autoresVivos.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Entrada inválida. Digite um ano numérico.");
        }
    }

    private void listarLivrosPorIdioma() {
        var menuIdiomas = """
                Digite o idioma desejado:
                es - Espanhol
                en - Inglês
                fr - Francês
                pt - Português
                """;
        System.out.println(menuIdiomas);
        var idioma = leitura.nextLine();

        List<Livro> livrosIdioma = livroRepository.findByIdioma(idioma);
        if (livrosIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma especificado.");
        } else {
            System.out.println("Livros no idioma " + idioma + ":");
            livrosIdioma.forEach(System.out::println);
        }
    }

    // ================== Estatísticas ==================
    private void gerarEstatisticasDownloads() {
        var livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("⚠️ Nenhum livro encontrado no banco.");
            return;
        }

        DoubleSummaryStatistics stats = livros.stream()
                .mapToDouble(l -> l.getNumeroDownloads() != null ? l.getNumeroDownloads() : 0)
                .summaryStatistics();

        System.out.println("\n📊 Estatísticas de downloads:");
        System.out.println("Total de livros: " + stats.getCount());
        System.out.println("Total de downloads: " + stats.getSum());
        System.out.println("Média de downloads: " + stats.getAverage());
        System.out.println("Livro com mais downloads: " + stats.getMax());
        System.out.println("Livro com menos downloads: " + stats.getMin());
    }

    private void listarTop10Livros() {
        var livros = livroRepository.findTop10ByOrderByNumeroDownloadsDesc();
        if (livros.isEmpty()) {
            System.out.println("⚠️ Nenhum livro encontrado.");
            return;
        }
        System.out.println("\n📚 Top 10 livros mais baixados:");
        livros.forEach(l -> System.out.println(l.getTitulo() + " - Downloads: " + l.getNumeroDownloads()));
    }

    // ================== Autor ==================
    private void buscarAutorPorNome() {
        System.out.println("Digite o nome (ou parte do nome) do autor:");
        var nome = leitura.nextLine();

        var autores = autorRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome);
        if (autores.isEmpty()) {
            System.out.println("⚠️ Nenhum autor encontrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresPorAnoNascimento() {
        System.out.println("Digite o ano mínimo de nascimento:");
        int ano = leitura.nextInt();
        leitura.nextLine();

        List<Autor> autores = autorRepository.findAutoresNascidosDepoisDe(ano);

        if (autores.isEmpty()) {
            System.out.println("⚠️ Nenhum autor encontrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresPorAnoFalecimento() {
        System.out.println("Digite o ano máximo de falecimento:");
        int ano = leitura.nextInt();
        leitura.nextLine();

        // Consulta segura (já corrigida no repositório)
        List<Autor> autores = autorRepository.findAutoresFalecidosAntesDe(ano);

        if (autores.isEmpty()) {
            System.out.println("⚠️ Nenhum autor encontrado com falecimento até " + ano);
        } else {
            System.out.println("Autores falecidos até o ano " + ano + ":");
            autores.forEach(System.out::println);
        }
    }

}





