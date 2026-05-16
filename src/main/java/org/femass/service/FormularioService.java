package org.femass.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.femass.dto.CursoDTO;
import org.femass.dto.FormularioDTO;
import org.femass.dto.RespostaDTO;
import org.femass.dto.SubjectDTO;
import org.femass.entity.Avaliacao;
import org.femass.entity.Curso;
import org.femass.entity.Disciplina;
import org.femass.entity.Pergunta;
import org.femass.entity.Resposta;
import org.femass.exception.CPFInvalidoException;
import org.femass.util.ValidacaoCPFUtil;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FormularioService {

    @Inject
    EntityManager entityManager;
    @Inject
    ValidacaoService validacaoService;

    @Transactional
    public void salvar(FormularioDTO formularioDTO) {
        if (formularioDTO == null) {
            throw new CPFInvalidoException("Formulário não pode ser nulo");
        }
        // Validar CPF do respondente (lança CPFInvalidoException em caso de problemas)
        validaCPF(formularioDTO);
        Curso curso = buscarOuCriarCurso(formularioDTO.course);
        if (curso == null) {
            return;
        }
        if (formularioDTO.subjects == null) {
            return;
        }

        // First pass: resolve all disciplinas. If any subject references a
        // disciplina that does not exist in the domain table, abort and return
        // a clear error (we don't want partial persistence).
        List<String> missing = new ArrayList<>();
        List<Disciplina> disciplinas = new ArrayList<>();
        for (SubjectDTO subjectDTO : formularioDTO.subjects) {
            Disciplina disciplina = buscarOuCriarDisciplina(subjectDTO, curso);
            if (disciplina == null) {
                String idOrName = subjectDTO.subjectId != null ? subjectDTO.subjectId : subjectDTO.subjectName;
                missing.add(idOrName == null ? "(sem identificador)" : idOrName);
            }
            disciplinas.add(disciplina);
        }

        if (!missing.isEmpty()) {
            // Throw a domain-level exception so the resource can return 400.
            throw new org.femass.exception.InvalidFormularioException("Disciplinas não encontradas: " + String.join(", ", missing));
        }

        // Second pass: now that all disciplinas are resolved, create e persistir avaliações
        for (int i = 0; i < formularioDTO.subjects.size(); i++) {
            SubjectDTO subjectDTO = formularioDTO.subjects.get(i);
            Disciplina disciplina = disciplinas.get(i);

            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setDisciplina(disciplina);
            avaliacao.setComentariosGerais(subjectDTO.comment);

            List<Resposta> respostas = new ArrayList<>();
            if (subjectDTO.answers != null) {
                for (RespostaDTO respostaDTO : subjectDTO.answers) {
                    Pergunta pergunta = buscarOuCriarPergunta(respostaDTO);
                    if (pergunta == null || respostaDTO.score == null) {
                        continue;
                    }
                    Resposta resposta = new Resposta();
                    resposta.setAvaliacao(avaliacao);
                    resposta.setPergunta(pergunta);
                    resposta.setNota(respostaDTO.score);
                    respostas.add(resposta);
                }
            }

            avaliacao.setRespostas(respostas);
            entityManager.persist(avaliacao);
        }
    }
     private void validaCPF(FormularioDTO formularioDTO) {
        // Validar CPF do respondente
         if (formularioDTO.respondent == null) {
             throw new CPFInvalidoException("Respondente não pode ser nulo");
         }

         if (formularioDTO.respondent.cpf == null || formularioDTO.respondent.cpf.isBlank()) {
             throw new CPFInvalidoException("CPF do respondente não pode ser vazio");
         }

         if (!ValidacaoCPFUtil.validarCPF(formularioDTO.respondent.cpf)) {
             throw new CPFInvalidoException("CPF inválido: " + formularioDTO.respondent.cpf);
         }
     }

    private Curso buscarOuCriarCurso(CursoDTO cursoDTO) {
        if (cursoDTO == null) {
            return null;
        }

        if (cursoDTO.name == null || cursoDTO.name.isBlank()) {
            return null;
        }

        Curso curso = null;
        if (cursoDTO.name != null && !cursoDTO.name.isBlank()) {
            curso = entityManager.createQuery(
                    "from Curso where nome = :nome", Curso.class)
                .setParameter("nome", cursoDTO.name)
                .getResultStream()
                .findFirst()
                .orElse(null);
        }

        if (curso == null) {
            curso = new Curso();
            curso.setNome(cursoDTO.name);
            entityManager.persist(curso);
        }

        return curso;
    }

    private Disciplina buscarOuCriarDisciplina(SubjectDTO subjectDTO, Curso curso) {
        if (subjectDTO == null || curso == null) {
            return null;
        }

        if (subjectDTO.subjectName == null || subjectDTO.subjectName.isBlank()) {
            return null;
        }

        if (subjectDTO.teacherName == null || subjectDTO.teacherName.isBlank()) {
            return null;
        }

        // Prefer lookup by subjectId when provided. If subjectId is numeric, try to
        // find by primary key. If not numeric or not found, fall back to name+professor lookup.
        Disciplina disciplina = null;

        if (subjectDTO.subjectId != null && !subjectDTO.subjectId.isBlank()) {
            try {
                Long did = Long.parseLong(subjectDTO.subjectId);
                disciplina = entityManager.find(Disciplina.class, did);
                if (disciplina != null) {
                    // verify it belongs to the same course
                    if (disciplina.getCurso() == null || disciplina.getCurso().getId() == null || !disciplina.getCurso().getId().equals(curso.getId())) {
                        disciplina = null;
                    }
                }
            } catch (NumberFormatException nfe) {
                // subjectId is not a number; fallthrough to name search below
            }
        }

        if (disciplina == null && subjectDTO.teacherName != null && !subjectDTO.teacherName.isBlank()) {
            disciplina = entityManager.createQuery(
                    "from Disciplina where id = :id",
                    Disciplina.class)
                .setParameter("id", subjectDTO.subjectId)
                .getResultStream()
                .findFirst()
                .orElse(null);
        }

        // If not found, return null so the caller will skip this subject.
        return disciplina;
    }

    private Pergunta buscarOuCriarPergunta(RespostaDTO respostaDTO) {
        if (respostaDTO == null || respostaDTO.questionText == null || respostaDTO.questionText.isBlank()) {
            return null;
        }

        Pergunta pergunta = null;
        if (respostaDTO.questionText != null && !respostaDTO.questionText.isBlank()) {
            pergunta = entityManager.createQuery(
                    "from Pergunta where texto = :texto", Pergunta.class)
                .setParameter("texto", respostaDTO.questionText)
                .getResultStream()
                .findFirst()
                .orElse(null);
        }

        if (pergunta == null) {
            pergunta = new Pergunta();
            pergunta.setTexto(respostaDTO.questionText);
            entityManager.persist(pergunta);
        }

        return pergunta;
    }
}
