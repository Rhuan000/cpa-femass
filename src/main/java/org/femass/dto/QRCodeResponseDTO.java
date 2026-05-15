package org.femass.dto;

public class QRCodeResponseDTO {
    private String qrCode;
    private String hash;

    public QRCodeResponseDTO(String qrCode, String hash) {
        this.qrCode = qrCode;
        this.hash = hash;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

