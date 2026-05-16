package org.femass.dto;

public class DisciplinaFormularioDTO {
    public Long id;
    public String nome;
    public String professor;

    public DisciplinaFormularioDTO() {
    }

    public DisciplinaFormularioDTO(Long id, String nome, String professor) {
        this.id = id;
        this.nome = nome;
        this.professor = professor;
    }
}
