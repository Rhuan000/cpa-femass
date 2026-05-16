package org.femass.dto;

public class PerguntaFormularioDTO {
    public Long id;
    public String texto;

    public PerguntaFormularioDTO() {
    }

    public PerguntaFormularioDTO(Long id, String texto) {
        this.id = id;
        this.texto = texto;
    }
}
