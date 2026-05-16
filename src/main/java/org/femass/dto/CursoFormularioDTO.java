package org.femass.dto;

import java.util.ArrayList;
import java.util.List;

public class CursoFormularioDTO {
    public Long id;
    public String nome;
    public List<DisciplinaFormularioDTO> disciplinas = new ArrayList<>();

    public CursoFormularioDTO() {
    }

    public CursoFormularioDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
