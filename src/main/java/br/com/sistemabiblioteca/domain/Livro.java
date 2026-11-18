package br.com.sistemabiblioteca.domain;

public class Livro extends MaterialAcervo {

    public Livro(String titulo, String autor, String isbn) {
        super(isbn, titulo, autor);
    }

    public String getIsbn() {
        return getCodigo();
    }

    public String getAutor() {
        return getDetalhes();
    }

    @Override
    public String getTipo() {
        return "Livro";
    }
}