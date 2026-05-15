package org.femass.dto;

public class ValidacaoResponseDTO {
    private String message;
    private Long id;
    private String hash;
    private String payload;

    public ValidacaoResponseDTO(String message, Long id, String hash, String payload) {
        this.message = message;
        this.id = id;
        this.hash = hash;
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

