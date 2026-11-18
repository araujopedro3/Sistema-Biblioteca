package br.com.sistemabiblioteca.domain;

public class Revista extends MaterialAcervo {

    public Revista(String titulo, String codigo, String editora, String edicao) {
        super(codigo, titulo, "Editora: " + editora + " | Edição: " + edicao);
    }

    @Override
    public String getTipo() {
        return "Revista";
    }
}