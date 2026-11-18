package br.com.sistemabiblioteca.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Biblioteca {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<MaterialAcervo> materiais = new ArrayList<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    public void cadastrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        validarDadosUsuario(
                usuario.getNome(),
                usuario.getIdade(),
                usuario.getCpf(),
                usuario.getEmail()
        );

        boolean cpfJaCadastrado = usuarios.stream()
                .anyMatch(u -> normalizarCpf(u.getCpf())
                        .equals(normalizarCpf(usuario.getCpf())));

        if (cpfJaCadastrado) {
            throw new IllegalArgumentException("Este CPF já está cadastrado.");
        }

        usuarios.add(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return List.copyOf(usuarios);
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        String cpfProcurado = normalizarCpf(cpf);

        for (Usuario usuario : usuarios) {
            if (normalizarCpf(usuario.getCpf()).equals(cpfProcurado)) {
                return usuario;
            }
        }
        return null;
    }

    public void atualizarUsuario(
            String id,
            String nome,
            int idade,
            String cpf,
            String email,
            TipoUsuario tipo
    ) {
        Usuario usuario = buscarUsuarioPorId(id);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        validarDadosUsuario(nome, idade, cpf, email);

        String cpfNormalizado = normalizarCpf(cpf);

        boolean cpfDuplicado = usuarios.stream()
                .anyMatch(outro -> !outro.getId().equalsIgnoreCase(id)
                        && normalizarCpf(outro.getCpf()).equals(cpfNormalizado));

        if (cpfDuplicado) {
            throw new IllegalArgumentException(
                    "Outro usuário já está cadastrado com esse CPF."
            );
        }

        usuario.setNome(nome.trim());
        usuario.setIdade(idade);
        usuario.setCpf(cpf.trim());
        usuario.setEmail(email.trim());
        usuario.setTipo(tipo);
    }

    public void desativarUsuario(String id) {
        Usuario usuario = buscarUsuarioPorId(id);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        boolean temEmprestimoAtivo = emprestimos.stream()
                .anyMatch(e -> e.getUsuario().getId().equalsIgnoreCase(id)
                        && e.isAtivo());

        if (temEmprestimoAtivo) {
            throw new IllegalStateException(
                    "Não é possível desativar usuário com empréstimo ativo."
            );
        }

        usuario.desativar();
    }

    public void cadastrarMaterial(MaterialAcervo material) {
        if (material == null) {
            throw new IllegalArgumentException("Material inválido.");
        }

        validarTexto(material.getTitulo(), "O título é obrigatório.");
        validarTexto(material.getCodigo(), "O código é obrigatório.");
        validarTexto(material.getDetalhes(), "Os detalhes são obrigatórios.");

        if (buscarMaterialPorCodigo(material.getCodigo()) != null) {
            throw new IllegalArgumentException(
                    "Já existe material cadastrado com esse código."
            );
        }

        materiais.add(material);
    }

    public List<MaterialAcervo> listarMateriais() {
        return List.copyOf(materiais);
    }

    public MaterialAcervo buscarMaterialPorCodigo(String codigo) {
        for (MaterialAcervo material : materiais) {
            if (material.getCodigo().equalsIgnoreCase(codigo)) {
                return material;
            }
        }
        return null;
    }

    public List<MaterialAcervo> buscarMateriaisPorTitulo(String titulo) {
        String busca = titulo.toLowerCase(Locale.ROOT);
        List<MaterialAcervo> encontrados = new ArrayList<>();

        for (MaterialAcervo material : materiais) {
            if (material.getTitulo().toLowerCase(Locale.ROOT).contains(busca)) {
                encontrados.add(material);
            }
        }

        return List.copyOf(encontrados);
    }

    public void atualizarMaterial(
            String codigo,
            String novoTitulo,
            String novosDetalhes
    ) {
        MaterialAcervo material = buscarMaterialPorCodigo(codigo);

        if (material == null) {
            throw new IllegalArgumentException("Material não encontrado.");
        }

        validarTexto(novoTitulo, "O título é obrigatório.");
        validarTexto(novosDetalhes, "Os detalhes são obrigatórios.");

        material.atualizarDados(novoTitulo.trim(), novosDetalhes.trim());
    }

    public void desativarMaterial(String codigo) {
        MaterialAcervo material = buscarMaterialPorCodigo(codigo);

        if (material == null) {
            throw new IllegalArgumentException("Material não encontrado.");
        }

        boolean temEmprestimoAtivo = emprestimos.stream()
                .anyMatch(e -> e.getMaterial().getCodigo().equalsIgnoreCase(codigo)
                        && e.isAtivo());

        if (temEmprestimoAtivo) {
            throw new IllegalStateException(
                    "Não é possível desativar material emprestado."
            );
        }

        material.desativar();
    }

    public Emprestimo realizarEmprestimo(String idUsuario, String codigoMaterial) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        MaterialAcervo material = buscarMaterialPorCodigo(codigoMaterial);

        if (usuario == null || !usuario.isAtivo()) {
            throw new IllegalArgumentException(
                    "Usuário não encontrado ou inativo."
            );
        }

        if (material == null || !material.isAtivo()) {
            throw new IllegalArgumentException(
                    "Material não encontrado ou inativo."
            );
        }

        if (!material.isDisponivel()) {
            throw new IllegalStateException("Material indisponível.");
        }

        Emprestimo emprestimo = new Emprestimo(usuario, material);
        emprestimos.add(emprestimo);
        material.marcarDisponivel(false);

        return emprestimo;
    }

    public List<Emprestimo> listarEmprestimos() {
        return List.copyOf(emprestimos);
    }

    public Emprestimo buscarEmprestimoPorId(String id) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getId().equalsIgnoreCase(id)) {
                return emprestimo;
            }
        }
        return null;
    }

    public Emprestimo devolverEmprestimo(String idEmprestimo) {
        Emprestimo emprestimo = buscarEmprestimoPorId(idEmprestimo);

        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado.");
        }

        emprestimo.devolver();
        return emprestimo;
    }

    private void validarDadosUsuario(
            String nome,
            int idade,
            String cpf,
            String email
    ) {
        validarTexto(nome, "O nome é obrigatório.");

        if (idade <= 0) {
            throw new IllegalArgumentException(
                    "A idade deve ser maior que zero."
            );
        }

        if (!normalizarCpf(cpf).matches("\\d{11}")) {
            throw new IllegalArgumentException(
                    "O CPF deve conter 11 dígitos."
            );
        }

        if (email == null
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    private String normalizarCpf(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    private void validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}