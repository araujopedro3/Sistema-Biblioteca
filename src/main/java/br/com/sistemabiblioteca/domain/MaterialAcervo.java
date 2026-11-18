package br.com.sistemabiblioteca.domain;

public abstract class MaterialAcervo {

    private final String codigo;
    private String titulo;
    private String detalhes;
    private boolean disponivel;
    private boolean ativo;

    protected MaterialAcervo(String codigo, String titulo, String detalhes) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.detalhes = detalhes;
        this.disponivel = true;
        this.ativo = true;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public boolean isDisponivel() {
        return ativo && disponivel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public abstract String getTipo();

    void atualizarDados(String titulo, String detalhes) {
        this.titulo = titulo;
        this.detalhes = detalhes;
    }

    void marcarDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    void desativar() {
        this.ativo = false;
        this.disponivel = false;
    }

    @Override
    public String toString() {
        return getTipo() + " | Código: " + codigo
                + " | Título: " + titulo
                + " | Detalhes: " + detalhes
                + " | " + (isDisponivel() ? "Disponível" : "Indisponível")
                + " | " + (ativo ? "Ativo" : "Inativo");
    }
}