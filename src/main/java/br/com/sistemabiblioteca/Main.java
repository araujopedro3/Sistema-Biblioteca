package br.com.sistemabiblioteca;

import br.com.sistemabiblioteca.domain.Biblioteca;
import br.com.sistemabiblioteca.domain.Emprestimo;
import br.com.sistemabiblioteca.domain.Livro;
import br.com.sistemabiblioteca.domain.MaterialAcervo;
import br.com.sistemabiblioteca.domain.MidiaDigital;
import br.com.sistemabiblioteca.domain.Revista;
import br.com.sistemabiblioteca.domain.TipoUsuario;
import br.com.sistemabiblioteca.domain.Usuario;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) {
        cadastrarDadosIniciais();

        boolean executando = true;

        while (executando) {
            mostrarMenuInicial();
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> menuPessoas();
                case "2" -> menuBiblioteca();
                case "0" -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static void cadastrarDadosIniciais() {
        Usuario usuario = new Usuario(
                "Pedro",
                20,
                "12345678900",
                "pedro@email.com",
                TipoUsuario.ALUNO
        );

        Livro livro = new Livro(
                "Java: Como Programar",
                "Paul Deitel",
                "9780133807806"
        );

        Revista revista = new Revista(
                "Revista de Tecnologia",
                "REV-001",
                "Editora Exemplo",
                "1"
        );

        biblioteca.cadastrarUsuario(usuario);
        biblioteca.cadastrarMaterial(livro);
        biblioteca.cadastrarMaterial(revista);

        System.out.println("Dados iniciais cadastrados.");
        System.out.println("ID do usuário inicial: " + usuario.getId());
        System.out.println("Código do livro inicial: " + livro.getCodigo());
    }

    private static void mostrarMenuInicial() {
        System.out.println("""
                
                ===== MENU INICIAL =====
                1 - Pessoas em geral
                2 - Biblioteca
                0 - Sair
                """);
        System.out.print("Escolha uma opção: ");
    }

    private static void menuPessoas() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("""
                    
                    ===== PESSOAS EM GERAL =====
                    1 - Cadastrar pessoa
                    2 - Listar pessoas
                    3 - Buscar pessoa por ID
                    4 - Atualizar pessoa
                    5 - Desativar pessoa
                    0 - Voltar ao menu inicial
                    """);
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> cadastrarUsuario();
                    case "2" -> listarUsuarios();
                    case "3" -> buscarUsuario();
                    case "4" -> atualizarUsuario();
                    case "5" -> desativarUsuario();
                    case "0" -> voltar = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException erro) {
                System.out.println("Não foi possível concluir: " + erro.getMessage());
            }
        }
    }

    private static void menuBiblioteca() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("""
                    
                    ===== BIBLIOTECA =====
                    1 - Cadastrar material
                    2 - Listar materiais
                    3 - Buscar material por código
                    4 - Atualizar material
                    5 - Desativar material
                    6 - Realizar empréstimo
                    7 - Listar empréstimos
                    8 - Buscar empréstimo por ID
                    9 - Registrar devolução
                    0 - Voltar ao menu inicial
                    """);
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> cadastrarMaterial();
                    case "2" -> listarMateriais();
                    case "3" -> buscarMaterial();
                    case "4" -> atualizarMaterial();
                    case "5" -> desativarMaterial();
                    case "6" -> realizarEmprestimo();
                    case "7" -> listarEmprestimos();
                    case "8" -> buscarEmprestimo();
                    case "9" -> devolverEmprestimo();
                    case "0" -> voltar = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException erro) {
                System.out.println("Não foi possível concluir: " + erro.getMessage());
            }
        }
    }

    private static void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        int idade = lerInteiro("Idade: ");

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        TipoUsuario tipo = lerTipoUsuario();

        Usuario usuario = new Usuario(nome, idade, cpf, email, tipo);
        biblioteca.cadastrarUsuario(usuario);

        System.out.println("Pessoa cadastrada. ID: " + usuario.getId());
    }

    private static void listarUsuarios() {
        if (biblioteca.listarUsuarios().isEmpty()) {
            System.out.println("Não há pessoas cadastradas.");
            return;
        }

        for (Usuario usuario : biblioteca.listarUsuarios()) {
            System.out.println(usuario);
        }
    }

    private static void buscarUsuario() {
        System.out.print("ID da pessoa: ");
        Usuario usuario = biblioteca.buscarUsuarioPorId(scanner.nextLine());

        if (usuario == null) {
            System.out.println("Pessoa não encontrada.");
        } else {
            System.out.println(usuario);
        }
    }

    private static void atualizarUsuario() {
        System.out.print("ID da pessoa que deseja atualizar: ");
        String id = scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        int idade = lerInteiro("Nova idade: ");

        System.out.print("Novo CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Novo e-mail: ");
        String email = scanner.nextLine();

        TipoUsuario tipo = lerTipoUsuario();

        biblioteca.atualizarUsuario(id, nome, idade, cpf, email, tipo);
        System.out.println("Pessoa atualizada.");
    }

    private static void desativarUsuario() {
        System.out.print("ID da pessoa: ");
        biblioteca.desativarUsuario(scanner.nextLine());
        System.out.println("Pessoa desativada.");
    }

    private static void cadastrarMaterial() {
        System.out.println("Tipo: 1-Livro, 2-Revista, 3-Mídia digital");
        System.out.print("Escolha: ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        MaterialAcervo material;

        switch (tipo) {
            case "1" -> {
                System.out.print("Autor: ");
                String autor = scanner.nextLine();

                System.out.print("ISBN: ");
                String isbn = scanner.nextLine();

                material = new Livro(titulo, autor, isbn);
            }
            case "2" -> {
                System.out.print("Código da revista: ");
                String codigo = scanner.nextLine();

                System.out.print("Editora: ");
                String editora = scanner.nextLine();

                System.out.print("Edição: ");
                String edicao = scanner.nextLine();

                material = new Revista(titulo, codigo, editora, edicao);
            }
            case "3" -> {
                System.out.print("Código da mídia: ");
                String codigo = scanner.nextLine();

                System.out.print("Formato: ");
                String formato = scanner.nextLine();

                System.out.print("Endereço ou link: ");
                String endereco = scanner.nextLine();

                material = new MidiaDigital(titulo, codigo, formato, endereco);
            }
            default -> throw new IllegalArgumentException(
                    "Tipo de material inválido."
            );
        }

        biblioteca.cadastrarMaterial(material);
        System.out.println("Material cadastrado.");
    }

    private static void listarMateriais() {
        if (biblioteca.listarMateriais().isEmpty()) {
            System.out.println("Não há materiais cadastrados.");
            return;
        }

        for (MaterialAcervo material : biblioteca.listarMateriais()) {
            System.out.println(material);
        }
    }

    private static void buscarMaterial() {
        System.out.print("Código do material: ");
        MaterialAcervo material =
                biblioteca.buscarMaterialPorCodigo(scanner.nextLine());

        if (material == null) {
            System.out.println("Material não encontrado.");
        } else {
            System.out.println(material);
        }
    }

    private static void atualizarMaterial() {
        System.out.print("Código do material: ");
        String codigo = scanner.nextLine();

        System.out.print("Novo título: ");
        String titulo = scanner.nextLine();

        System.out.print("Novos detalhes: ");
        String detalhes = scanner.nextLine();

        biblioteca.atualizarMaterial(codigo, titulo, detalhes);
        System.out.println("Material atualizado.");
    }

    private static void desativarMaterial() {
        System.out.print("Código do material: ");
        biblioteca.desativarMaterial(scanner.nextLine());
        System.out.println("Material desativado.");
    }

    private static void realizarEmprestimo() {
        System.out.print("ID da pessoa: ");
        String idUsuario = scanner.nextLine();

        System.out.print("Código do material: ");
        String codigo = scanner.nextLine();

        Emprestimo emprestimo =
                biblioteca.realizarEmprestimo(idUsuario, codigo);

        System.out.println("Empréstimo registrado:");
        System.out.println(emprestimo);
    }

    private static void listarEmprestimos() {
        if (biblioteca.listarEmprestimos().isEmpty()) {
            System.out.println("Não há empréstimos registrados.");
            return;
        }

        for (Emprestimo emprestimo : biblioteca.listarEmprestimos()) {
            System.out.println(emprestimo);
        }
    }

    private static void buscarEmprestimo() {
        System.out.print("ID do empréstimo: ");
        Emprestimo emprestimo =
                biblioteca.buscarEmprestimoPorId(scanner.nextLine());

        if (emprestimo == null) {
            System.out.println("Empréstimo não encontrado.");
        } else {
            System.out.println(emprestimo);
        }
    }

    private static void devolverEmprestimo() {
        System.out.print("ID do empréstimo: ");
        Emprestimo emprestimo =
                biblioteca.devolverEmprestimo(scanner.nextLine());

        System.out.println("Devolução registrada.");
        System.out.printf("Multa: R$ %.2f%n", emprestimo.getMulta());
    }

    private static TipoUsuario lerTipoUsuario() {
        System.out.println("Tipos: ALUNO, PROFESSOR ou COMUNIDADE");
        System.out.print("Tipo da pessoa: ");

        try {
            return TipoUsuario.valueOf(
                    scanner.nextLine().trim().toUpperCase()
            );
        } catch (IllegalArgumentException erro) {
            throw new IllegalArgumentException(
                    "Tipo inválido. Use ALUNO, PROFESSOR ou COMUNIDADE."
            );
        }
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        String valor = scanner.nextLine();

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                    "Digite um número inteiro válido."
            );
        }
    }
}