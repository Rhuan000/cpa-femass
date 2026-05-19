package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class QRCodeResourceTest {

    @Test
    void deveGerarCodigoParaQRCode() {
        String qrCode = given()
                .contentType("application/json")
                .body("""
                        {
                          "cpf": "1234",
                          "matricula": "20260001",
                          "aceiteTermosCondicoesServico": true,
                          "cursos": ["Sistemas da Informacao"],
                          "disciplinas": ["ALG", "SIS"],
                          "identificador": "11111111-1111-1111-1111-111111111111"
                        }
                        """)
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(200)
                .body("hash", notNullValue())
                .body("qrCode", notNullValue())
                .extract()
                .path("qrCode");

        given()
                .contentType("text/plain")
                .body(qrCode)
                .when().post("/qrcode/decodificar")
                .then()
                .statusCode(200)
                .body("cpf", is("1234"))
                .body("matricula", is("20260001"))
                .body("aceiteTermosCondicoesServico", is(true))
                .body("cursos[0]", is("Sistemas da Informacao"))
                .body("disciplinas[0]", is("ALG"))
                .body("disciplinas[1]", is("SIS"))
                .body("identificador", is("11111111-1111-1111-1111-111111111111"));
    }

    @Test
    void deveRetornarErroQuandoPayloadForVazio() {
        given()
                .contentType("application/json")
                .body("")
                .when().post("/qrcode/gerar")
                .then()
                .statusCode(400)
                .body("error", is("Payload do QR Code e obrigatorio"));
    }

    @Test
    void deveRetornarErroQuandoCodigoForInvalido() {
        given()
                .contentType("text/plain")
                .body("codigo-invalido")
                .when().post("/qrcode/decodificar")
                .then()
                .statusCode(400)
                .body("error", is("Codigo do QR Code invalido"));
    }
}
