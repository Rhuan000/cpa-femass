package org.femass.dto;

import java.util.List;

public class QRCodePayloadDTO {
    public String cpf;
    public String matricula;
    public List<String> cursos;
    public List<String> disciplinas;
    public String identificador;

    public QRCodePayloadDTO() {
    }

    public QRCodePayloadDTO(String cpf, String matricula, List<String> cursos, List<String> disciplinas, String identificador) {
        this.cpf = cpf;
        this.matricula = matricula;
        this.cursos = cursos;
        this.disciplinas = disciplinas;
        this.identificador = identificador;
    }
}
