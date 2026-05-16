package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class FormularioResourceTest {

    @Test
    void deveSalvarFormularioERetornarHash() {
        String formulario = """
                {
                  "schemaVersion": "1",
                  "confirmationCode": "confirmacao-teste",
                  "course": {
                    "name": "Curso Teste Formulario"
                  },
                  "subjects": [
                    {
                      "subjectName": "Disciplina Teste Formulario",
                      "teacherName": "Professor Teste",
                      "answers": [
                        {
                          "questionText": "Pergunta teste?",
                          "score": 5
                        }
                      ],
                      "comment": "Comentario teste"
                    }
                  ]
                }
                """;

        given()
                .contentType("application/json")
                .body(formulario)
                .when().post("/formulario")
                .then()
                .statusCode(200)
                .body("hash", matchesPattern("^[a-f0-9]{64}$"))
                .body("qrCode", matchesPattern("^[a-f0-9]{64}$"));
    }
}
