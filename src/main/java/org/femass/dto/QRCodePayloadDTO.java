package org.femass.dto;

import java.util.List;

public class QRCodePayloadDTO {
    public String cpf;
    public String matricula;
    public Boolean aceiteTermosCondicoesServico;
    public List<String> cursos;
    public List<String> disciplinas;
    public String identificador;

    public QRCodePayloadDTO() {
    }

    public QRCodePayloadDTO(String cpf, String matricula, Boolean aceiteTermosCondicoesServico, List<String> cursos, List<String> disciplinas, String identificador) {
        this.cpf = cpf;
        this.matricula = matricula;
        this.aceiteTermosCondicoesServico = aceiteTermosCondicoesServico;
        this.cursos = cursos;
        this.disciplinas = disciplinas;
        this.identificador = identificador;
    }
}
