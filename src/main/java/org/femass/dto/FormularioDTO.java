package org.femass.dto;

import java.util.ArrayList;
import java.util.Date;

/// Esse DTO é baseado no json que o Joshua mandou no zap
public class FormularioDTO {
    public String schemaVersion;
    public String confirmationCode;
    public Date submittedAt;
    public UsuarioDTO respondent; //Quem respondeu ao formulário
    public CursoDTO course;
    public ArrayList<SubjectDTO> subjects;

}
