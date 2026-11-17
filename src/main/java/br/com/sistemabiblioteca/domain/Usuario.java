package br.com.sistemabiblioteca.domain;

public class Usuario extends Pessoa {

    private String id;

    public Usuario(String nome, int idade, String cpf,
                   String email, String id) {
        super(nome, idade, cpf, email);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}