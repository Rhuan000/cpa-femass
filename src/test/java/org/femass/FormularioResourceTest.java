package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
class FormularioResourceTest {

    @Test
    void deveSalvarFormularioERetornarHash() {
        String formulario = """
                {
                  "schemaVersion": "1",
                  "confirmationCode": "confirmacao-teste",
                  "respondent": {
                    "cpf": "123.456.789-00",
                    "matricula": "20260001"
                  },
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

        JsonPath response = given()
                .contentType("application/json")
                .body(formulario)
                .when().post("/formulario")
                .then()
                .statusCode(200)
                .body("hash", notNullValue())
                .body("qrCode", notNullValue())
                .extract()
                .jsonPath();

        org.hamcrest.MatcherAssert.assertThat(response.getString("hash"), equalTo(response.getString("qrCode")));
    }

    @Test
    void deveRetornarQRCodeDecodificavelComDadosDoRespondenteEDisciplinas() {
        String formulario = """
                {
                  "schemaVersion": "1",
                  "confirmationCode": "confirmacao-teste-decodificacao",
                  "respondent": {
                    "cpf": "987.654.321-00",
                    "matricula": "20260002"
                  },
                  "course": {
                    "name": "Curso Teste QR"
                  },
                  "subjects": [
                    {
                      "subjectName": "Álgebra Linear",
                      "teacherName": "Professor Teste",
                      "answers": [
                        {
                          "questionText": "Pergunta teste QR?",
                          "score": 4
                        }
                      ]
                    },
                    {
                      "subjectName": "Sistemas Operacionais",
                      "teacherName": "Professor Teste",
                      "answers": [
                        {
                          "questionText": "Pergunta teste QR 2?",
                          "score": 5
                        }
                      ]
                    }
                  ]
                }
                """;

        String qrCode = given()
                .contentType("application/json")
                .body(formulario)
                .when().post("/formulario")
                .then()
                .statusCode(200)
                .body("hash", notNullValue())
                .body("qrCode", notNullValue())
                .extract()
                .path("qrCode");

        String payload = new String(Base64.getUrlDecoder().decode(qrCode), StandardCharsets.UTF_8);

        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.containsString("\"cpf\":\"9876\""));
        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.containsString("\"matricula\":\"20260002\""));
        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.containsString("\"ALG\""));
        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.containsString("\"SIS\""));
        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.containsString("\"identificador\":"));
        org.hamcrest.MatcherAssert.assertThat(payload, org.hamcrest.Matchers.matchesPattern(".*\"identificador\":\"[a-f0-9-]{36}\"\\}$"));
    }

    @Test
    void deveGerarCodigosDiferentesParaFormulariosIguais() {
        String formulario = """
                {
                  "schemaVersion": "1",
                  "confirmationCode": "confirmacao-teste-unico",
                  "respondent": {
                    "cpf": "111.222.333-44",
                    "matricula": "20260003"
                  },
                  "course": {
                    "name": "Curso Teste Codigo Unico"
                  },
                  "subjects": [
                    {
                      "subjectName": "Calculo Numerico",
                      "teacherName": "Professor Teste",
                      "answers": [
                        {
                          "questionText": "Pergunta codigo unico?",
                          "score": 5
                        }
                      ]
                    }
                  ]
                }
                """;

        String primeiroCodigo = given()
                .contentType("application/json")
                .body(formulario)
                .when().post("/formulario")
                .then()
                .statusCode(200)
                .extract()
                .path("qrCode");

        String segundoCodigo = given()
                .contentType("application/json")
                .body(formulario)
                .when().post("/formulario")
                .then()
                .statusCode(200)
                .extract()
                .path("qrCode");

        org.hamcrest.MatcherAssert.assertThat(segundoCodigo, not(equalTo(primeiroCodigo)));
    }
}
