package br.com.sistemabiblioteca.domain;

public class MidiaDigital extends MaterialAcervo {

    public MidiaDigital(
            String titulo,
            String codigo,
            String formato,
            String endereco
    ) {
        super(codigo, titulo, "Formato: " + formato + " | Endereço: " + endereco);
    }

    @Override
    public String getTipo() {
        return "Mídia digital";
    }
}