package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
                .body("hash", equalTo("{\"formulario\":\"teste-qrcode\"}"))
                .body("qrCode", equalTo("{\"formulario\":\"teste-qrcode\"}"));
    }

    @Test
    void deveRetornarErroQuandoPayloadForVazio() {
        given()
                .contentType("application/json")
                .body("")
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(400)
                .body("error", is("Codigo de validacao e obrigatorio"));
    }
}
