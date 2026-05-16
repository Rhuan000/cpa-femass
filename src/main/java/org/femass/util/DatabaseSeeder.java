package org.femass.util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@ApplicationScoped
public class DatabaseSeeder {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void onStart(@Observes StartupEvent event) {
        if (dadosIniciaisJaExistem()) {
            return;
        }

        executarScriptInicial();
    }

    private boolean dadosIniciaisJaExistem() {
        Long totalCursos = entityManager.createQuery("select count(c) from Curso c", Long.class)
                .getSingleResult();
        Long totalDisciplinas = entityManager.createQuery("select count(d) from Disciplina d", Long.class)
                .getSingleResult();

        return totalCursos > 0 || totalDisciplinas > 0;
    }

    private void executarScriptInicial() {
        InputStream script = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("import.sql");

        if (script == null) {
            throw new IllegalStateException("Arquivo import.sql nao encontrado para seed inicial");
        }

        String sql = new BufferedReader(new InputStreamReader(script, StandardCharsets.UTF_8))
                .lines()
                .filter(line -> !line.trim().startsWith("--"))
                .collect(Collectors.joining("\n"));

        for (String statement : sql.split(";")) {
            String trimmedStatement = statement.trim();
            if (!trimmedStatement.isEmpty()) {
                if (trimmedStatement.toLowerCase().startsWith("select")) {
                    entityManager.createNativeQuery(trimmedStatement).getSingleResult();
                } else {
                    entityManager.createNativeQuery(trimmedStatement).executeUpdate();
                }
            }
        }
    }
}
