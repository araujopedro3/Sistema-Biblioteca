package br.com.sistemabiblioteca.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private static int contador = 0;

    private final String id;
    private final Usuario usuario;
    private final MaterialAcervo material;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataDevolucaoPrevista;

    private LocalDate dataDevolucaoReal;
    private double multa;

    public Emprestimo(Usuario usuario, MaterialAcervo material) {
        if (usuario == null || material == null) {
            throw new IllegalArgumentException(
                    "Empréstimo exige usuário e material válidos."
            );
        }

        contador++;
        this.id = String.format("EMP-%03d", contador);
        this.usuario = usuario;
        this.material = material;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(7);
        this.dataDevolucaoReal = null;
        this.multa = 0.0;
    }

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public MaterialAcervo getMaterial() {
        return material;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public double getMulta() {
        return multa;
    }

    public boolean isAtivo() {
        return dataDevolucaoReal == null;
    }

    public void devolver() {
        if (!isAtivo()) {
            throw new IllegalStateException(
                    "Este empréstimo já foi devolvido."
            );
        }

        dataDevolucaoReal = LocalDate.now();

        long diasAtraso = ChronoUnit.DAYS.between(
                dataDevolucaoPrevista,
                dataDevolucaoReal
        );

        multa = Math.max(0L, diasAtraso) * 2.0;
        material.marcarDisponivel(true);
    }

    @Override
    public String toString() {
        String situacao = isAtivo() ? "Ativo" : "Devolvido";

        return id
                + " | Usuário: " + usuario.getNome() + " (" + usuario.getId() + ")"
                + " | Material: " + material.getTitulo()
                + " | Devolução prevista: " + dataDevolucaoPrevista
                + " | Situação: " + situacao
                + " | Multa: R$ " + String.format("%.2f", multa);
    }
}