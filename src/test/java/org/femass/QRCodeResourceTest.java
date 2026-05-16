package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class QRCodeResourceTest {

    @Test
    void deveGerarHashParaQRCode() {
        given()
                .contentType("application/json")
                .body("{\"formulario\":\"teste-qrcode\"}")
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(200)
                .body("hash", matchesPattern("^[a-f0-9]{64}$"))
                .body("qrCode", notNullValue());
    }

    @Test
    void deveRetornarErroQuandoPayloadForVazio() {
        given()
                .contentType("application/json")
                .body("")
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(400)
                .body("error", is("Payload e obrigatorio para gerar o hash"));
    }
}
