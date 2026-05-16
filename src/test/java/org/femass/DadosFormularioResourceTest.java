package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class DadosFormularioResourceTest {

    @Test
    void deveBuscarDadosParaPopularFormulario() {
        given()
                .when().get("/dados-formulario")
                .then()
                .statusCode(200)
                .body("cursos", notNullValue())
                .body("cursos.size()", greaterThan(0))
                .body("cursos[0].id", notNullValue())
                .body("cursos[0].nome", notNullValue())
                .body("cursos[0].disciplinas", notNullValue())
                .body("perguntas", notNullValue());
    }
}
