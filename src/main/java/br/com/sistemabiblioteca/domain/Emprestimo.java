package br.com.sistemabiblioteca.domain;

import java.time.LocalDate;

public class Emprestimo {
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean ativo;
    private double multa;

    public Emprestimo(Livro livro, Usuario usuario){
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = LocalDate.now().plusDays(7);
        this.ativo = true;
        this.multa = 0.0;
    }

    public void devolver() {
        if (!ativo) {
            throw new IllegalStateException("Este empréstimo já foi encerrado.");
        }
        this.ativo = false;
    }
}
