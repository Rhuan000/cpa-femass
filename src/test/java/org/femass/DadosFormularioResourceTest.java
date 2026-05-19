package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
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
                .body("perguntas", notNullValue())
                .body("perguntas.size()", equalTo(10))
                .body("perguntas[0].id", equalTo("q1"))
                .body("perguntas[9].id", equalTo("q10"));
    }

    @Test
    void deveBuscarPerguntasParaPopularFormulario() {
        given()
                .when().get("/perguntas")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("[0].id", equalTo("q1"))
                .body("[0].texto", equalTo("Promove o debate e instiga o pensamento crítico, colaborando para a autonomia dos estudantes."))
                .body("[9].id", equalTo("q10"))
                .body("[9].texto", equalTo("Utiliza diferentes estratégias pedagógicas ou andragógicas que incentivam a aprendizagem e a pesquisa."));
    }
}
