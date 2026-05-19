package org.femass.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.femass.dto.CursoFormularioDTO;
import org.femass.dto.DadosFormularioDTO;
import org.femass.dto.DisciplinaFormularioDTO;
import org.femass.dto.PerguntaFormularioDTO;
import org.femass.entity.Curso;
import org.femass.entity.Disciplina;
import org.femass.entity.Pergunta;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DadosFormularioService {

    @Inject
    EntityManager entityManager;

    public DadosFormularioDTO buscarDadosFormulario() {
        Map<Long, CursoFormularioDTO> cursos = buscarCursos();
        preencherDisciplinas(cursos);

        return new DadosFormularioDTO(
                List.copyOf(cursos.values()),
                buscarPerguntas()
        );
    }

    private Map<Long, CursoFormularioDTO> buscarCursos() {
        List<Curso> cursos = entityManager.createQuery(
                "from Curso order by nome",
                Curso.class
        ).getResultList();

        Map<Long, CursoFormularioDTO> cursosDTO = new LinkedHashMap<>();
        for (Curso curso : cursos) {
            cursosDTO.put(curso.getId(), new CursoFormularioDTO(curso.getId(), curso.getNome()));
        }

        return cursosDTO;
    }

    private void preencherDisciplinas(Map<Long, CursoFormularioDTO> cursos) {
        List<Disciplina> disciplinas = entityManager.createQuery(
                "from Disciplina d join fetch d.curso order by d.curso.nome, d.nome",
                Disciplina.class
        ).getResultList();

        for (Disciplina disciplina : disciplinas) {
            CursoFormularioDTO curso = cursos.get(disciplina.getCurso().getId());
            if (curso != null) {
                curso.disciplinas.add(new DisciplinaFormularioDTO(
                        disciplina.getId(),
                        disciplina.getNome(),
                        disciplina.getProfessor()
                ));
            }
        }
    }

    public List<PerguntaFormularioDTO> buscarPerguntas() {
        return entityManager.createQuery(
                "from Pergunta where codigo is not null",
                Pergunta.class
        ).getResultStream()
                .sorted(Comparator.comparingInt(this::ordemPergunta))
                .map(pergunta -> new PerguntaFormularioDTO(pergunta.getCodigo(), pergunta.getTexto()))
                .toList();
    }

    private int ordemPergunta(Pergunta pergunta) {
        String codigo = pergunta.getCodigo();
        if (codigo == null || codigo.length() < 2) {
            return Integer.MAX_VALUE;
        }

        try {
            return Integer.parseInt(codigo.substring(1));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}
