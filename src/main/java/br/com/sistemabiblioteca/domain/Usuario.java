package br.com.sistemabiblioteca.domain;

public class Usuario extends Pessoa {

    private static int contador = 0;

    private final String id;
    private TipoUsuario tipo;
    private boolean ativo;

    public Usuario(
            String nome,
            int idade,
            String cpf,
            String email,
            TipoUsuario tipo
    ) {
        super(nome, idade, cpf, email);

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de usuário é obrigatório.");
        }

        contador++;
        this.id = String.format("USR-%03d", contador);
        this.tipo = tipo;
        this.ativo = true;
    }

    public String getId() {
        return id;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de usuário é obrigatório.");
        }
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    void desativar() {
        ativo = false;
    }

    @Override
    public String toString() {
        return id + " | " + getNome()
                + " | " + tipo
                + " | CPF: " + getCpf()
                + " | " + (ativo ? "Ativo" : "Inativo");
    }
}