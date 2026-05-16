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

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FormularioService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(FormularioDTO formularioDTO) {
        if (formularioDTO == null) {
            return;
        }

        Curso curso = buscarOuCriarCurso(formularioDTO.course);
        if (curso == null) {
            return;
        }

        if (formularioDTO.subjects == null) {
            return;
        }

        for (SubjectDTO subjectDTO : formularioDTO.subjects) {
            Disciplina disciplina = buscarOuCriarDisciplina(subjectDTO, curso);
            if (disciplina == null) {
                continue;
            }
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

        Disciplina disciplina = null;
        if (subjectDTO.teacherName != null && !subjectDTO.teacherName.isBlank()) {
            disciplina = entityManager.createQuery(
                    "from Disciplina where nome = :nome and professor = :professor and curso = :curso",
                    Disciplina.class)
                .setParameter("nome", subjectDTO.subjectName)
                .setParameter("professor", subjectDTO.teacherName)
                .setParameter("curso", curso)
                .getResultStream()
                .findFirst()
                .orElse(null);
        }

        if (disciplina == null) {
            disciplina = new Disciplina();
            disciplina.setNome(subjectDTO.subjectName);
            disciplina.setProfessor(subjectDTO.teacherName);
            disciplina.setCurso(curso);
            entityManager.persist(disciplina);
        }

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
