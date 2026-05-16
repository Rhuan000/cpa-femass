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
}