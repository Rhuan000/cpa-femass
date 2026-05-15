package org.femass.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;


@Entity
public class Validacao extends PanacheEntity {
    private String hash;
    private Boolean validado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataValidacao;
    private Integer tentativasValidacao;

    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
        if (this.validado == null) {
            this.validado = false;
        }
        if (this.tentativasValidacao == null) {
            this.tentativasValidacao = 0;
        }
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataValidacao() {
        return dataValidacao;
    }

    public void setDataValidacao(LocalDateTime dataValidacao) {
        this.dataValidacao = dataValidacao;
    }

    public Integer getTentativasValidacao() {
        return tentativasValidacao;
    }

    public void setTentativasValidacao(Integer tentativasValidacao) {
        this.tentativasValidacao = tentativasValidacao;
    }
}

