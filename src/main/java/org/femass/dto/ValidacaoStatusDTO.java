package org.femass.dto;

public class ValidacaoStatusDTO {
    private String hash;
    private Boolean validado;
    private String status;
    private String mensagem;

    public ValidacaoStatusDTO(String hash, Boolean validado, String status, String mensagem) {
        this.hash = hash;
        this.validado = validado;
        this.status = status;
        this.mensagem = mensagem;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

