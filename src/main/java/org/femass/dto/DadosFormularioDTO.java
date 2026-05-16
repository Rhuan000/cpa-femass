package org.femass.dto;

import java.util.List;

public class DadosFormularioDTO {
    public List<CursoFormularioDTO> cursos;
    public List<PerguntaFormularioDTO> perguntas;

    public DadosFormularioDTO() {
    }

    public DadosFormularioDTO(List<CursoFormularioDTO> cursos, List<PerguntaFormularioDTO> perguntas) {
        this.cursos = cursos;
        this.perguntas = perguntas;
    }
}
