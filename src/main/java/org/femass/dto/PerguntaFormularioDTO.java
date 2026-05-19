package org.femass.dto;

public class PerguntaFormularioDTO {
    public String id;
    public String texto;

    public PerguntaFormularioDTO() {
    }

    public PerguntaFormularioDTO(String id, String texto) {
        this.id = id;
        this.texto = texto;
    }
}
