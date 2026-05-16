package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ValidacaoResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/validacao/verificar-status")
          .then()
             .statusCode(400)
             .body("error", is("Hash é obrigatório como parâmetro de query"));
    }
    @Test
    void deveValidarHashERetornarDadosDecodificadosParaMobile() {
        String codigo = given()
                .contentType("application/json")
                .body("""
                        {
                          "cpf": "1234",
                          "matricula": "20260001",
                          "cursos": ["Sistemas da Informacao"],
                          "disciplinas": ["ALG", "SIS"],
                          "identificador": "22222222-2222-2222-2222-222222222222"
                        }
                        """)
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(200)
                .extract()
                .path("hash");

        given()
                .queryParam("hash", codigo)
                .when().put("/validacao/validar-hash")
                .then()
                .statusCode(200)
                .body("cpf", is("1234"))
                .body("matricula", is("20260001"))
                .body("cursos[0]", is("Sistemas da Informacao"))
                .body("disciplinas[0]", is("ALG"))
                .body("disciplinas[1]", is("SIS"));
    }
}
