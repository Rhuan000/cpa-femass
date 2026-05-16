package org.femass;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Testes Unitários - ValidacaoResource")
class ValidacaoResourceTest {

    // ==================== TESTES: Validação de Parâmetros ====================

    @Test
    @DisplayName("Deve retornar erro 400 quando hash está ausente em verificarStatus")
    void testHelloEndpoint() {
        given()
          .when().get("/validacao/verificar-status")
          .then()
             .statusCode(400)
             .body("error", is("Hash é obrigatório como parâmetro de query"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando hash é null em verificarStatus")
    void testVerificarStatusHashNull() {
        given()
            .queryParam("hash", (String) null)
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(400)
            .body("error", is("Hash é obrigatório como parâmetro de query"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando hash é empty string em validarHash")
    void testValidarHashEmptyString() {
        given()
            .queryParam("hash", "")
        .when()
            .put("/validacao/validar-hash")
        .then()
            .statusCode(400)
            .body("error", is("Hash é obrigatório como parâmetro de query"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando hash é vazio em buscarHash")
    void testBuscarHashEmptyString() {
        given()
            .queryParam("hash", "")
        .when()
            .get("/validacao/buscar-hash")
        .then()
            .statusCode(400)
            .body("error", is("Hash é obrigatório como parâmetro de query"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando hash é apenas espaços em branco")
    void testHashComApenasEspacosBrancos() {
        given()
            .queryParam("hash", "     ")
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(400)
            .body("error", is("Hash é obrigatório como parâmetro de query"));
    }

    // ==================== TESTES: Estrutura de Resposta ====================

    @Test
    @DisplayName("Deve retornar JSON válido em armazenarHash")
    void testArmazenarHashRetornaJson() {
        given()
            .contentType(ContentType.TEXT)
            .body("test-payload-" + System.currentTimeMillis())
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("$", hasKey("message"))
            .body("$", hasKey("hash"))
            .body("$", hasKey("id"))
            .body("$", hasKey("payload"));
    }

    @Test
    @DisplayName("Deve retornar mensagem de erro em JSON válido")
    void testErroRetornaJsonValido() {
        given()
            .queryParam("hash", "hash_invalido_12345")
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("$", hasKey("error"))
            .body("error", notNullValue())
            .body("error", not(emptyString()));
    }

    // ==================== TESTES: HTTP Status Codes ====================

    @Test
    @DisplayName("Deve retornar 200 ao armazenar hash com sucesso")
    void testArmazenarHashStatus200() {
        given()
            .contentType(ContentType.TEXT)
            .body("payload-status-" + System.nanoTime())
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Deve retornar 404 para hash não encontrado em verificarStatus")
    void testVerificarStatusHashNaoEncontrado404() {
        given()
            .queryParam("hash", "nao_existe_" + System.nanoTime())
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Deve retornar 404 para hash não encontrado em validarHash")
    void testValidarHashNaoEncontrado404() {
        given()
            .queryParam("hash", "nao_existe_validar_" + System.nanoTime())
        .when()
            .put("/validacao/validar-hash")
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Deve retornar 404 para hash não encontrado em buscarHash")
    void testBuscarHashNaoEncontrado404() {
        given()
            .queryParam("hash", "nao_existe_busca_" + System.nanoTime())
        .when()
            .get("/validacao/buscar-hash")
        .then()
            .statusCode(404);
    }

    // ==================== TESTES: Consistência de Dados ====================

    @Test
    @DisplayName("Deve manter o mesmo hash para o mesmo payload em multiplas requisições")
    void testHashConsistenceMultipleRequests() {
        String payload = "payload-consistencia-" + System.nanoTime();

        String hash1 = given()
            .contentType(ContentType.TEXT)
            .body(payload)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        String hash2 = given()
            .contentType(ContentType.TEXT)
            .body(payload)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        assertEquals(hash1, hash2, "Hashes do mesmo payload devem ser iguais");
    }

    @Test
    @DisplayName("Deve retornar payload correto em armazenarHash")
    void testPayloadPreservado() {
        String payloadOriginal = "payload-preservacao-" + System.nanoTime();

        String payloadRetornado = given()
            .contentType(ContentType.TEXT)
            .body(payloadOriginal)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("payload");

        assertEquals(payloadOriginal, payloadRetornado, "Payload retornado deve ser igual ao enviado");
    }

    @Test
    @DisplayName("Deve validar hash com a mensagem correta")
    void testMensagemValidacaoCorreta() {
        String payload = "payload-msg-" + System.nanoTime();

        String hash = given()
            .contentType(ContentType.TEXT)
            .body(payload)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        given()
            .queryParam("hash", hash)
        .when()
            .put("/validacao/validar-hash")
        .then()
            .statusCode(200)
            .body("message", equalTo("Hash validado com sucesso!"));
    }

    // ==================== TESTES: Content-Type ====================

    @Test
    @DisplayName("Deve retornar Content-Type JSON em armazenarHash")
    void testContentTypeArmazenarHash() {
        given()
            .contentType(ContentType.TEXT)
            .body("test-payload")
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Deve retornar Content-Type JSON em verificarStatus")
    void testContentTypeVerificarStatus() {
        String payload = "test-" + System.nanoTime();

        String hash = given()
            .contentType(ContentType.TEXT)
            .body(payload)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        given()
            .queryParam("hash", hash)
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    // ==================== TESTES: Campos Obrigatórios ====================

    @Test
    @DisplayName("Hash retornado em armazenarHash não pode ser nulo")
    void testHashNaoNuloEmArmazenar() {
        String hash = given()
            .contentType(ContentType.TEXT)
            .body("payload-not-null-" + System.nanoTime())
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        assertNotNull(hash, "Hash não pode ser nulo");
        assertFalse(hash.isEmpty(), "Hash não pode estar vazio");
    }

    @Test
    @DisplayName("Message retornada em armazenarHash não pode ser nula")
    void testMessageNaoNulaEmArmazenar() {
        given()
            .contentType(ContentType.TEXT)
            .body("payload-message-" + System.nanoTime())
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .body("message", notNullValue())
            .body("message", not(emptyString()));
    }

    @Test
    @DisplayName("Error retornado deve ter mensagem descritiva")
    void testErrorMessageDescritiva() {
        given()
            .queryParam("hash", "hash_invalido")
        .when()
            .get("/validacao/verificar-status")
        .then()
            .statusCode(404)
            .body("error", containsString("não encontrado"));
    }

    // ==================== TESTES: Segurança ====================

    @Test
    @DisplayName("Deve aceitar hash com caracteres hexadecimais válidos")
    void testHashHexadecimalValido() {
        String payload = "test-hex-" + System.nanoTime();

        String hash = given()
            .contentType(ContentType.TEXT)
            .body(payload)
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        // Valida que o hash contém apenas caracteres hexadecimais
        assertTrue(hash.matches("[a-fA-F0-9]+"), "Hash deve conter apenas caracteres hexadecimais");
    }

    @Test
    @DisplayName("Hash gerado deve ter tamanho consistente")
    void testTamanhoHashConsistente() {
        String hash1 = given()
            .contentType(ContentType.TEXT)
            .body("payload-1")
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        String hash2 = given()
            .contentType(ContentType.TEXT)
            .body("payload-2")
        .when()
            .post("/validacao/armazenar-hash")
        .then()
            .statusCode(200)
            .extract().jsonPath().getString("hash");

        assertEquals(hash1.length(), hash2.length(), "Todos os hashes devem ter o mesmo tamanho");
    }
}