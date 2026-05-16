package org.femass.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "VALIDACAO")
public class Validacao extends PanacheEntityBase {
    @Id
    @Column(name = "HASH", length = 64, nullable = false, unique = true)
    private String hash;

    @Column(name = "VALIDADO", nullable = false)
    private Boolean validado;

    @Column(name = "DATA_CRIACAO", nullable = true)
    private LocalDateTime dataCriacao;

    @Column(name = "DATA_VALIDACAO", nullable = true)
    private LocalDateTime dataValidacao;

    @Column(name = "TENTATIVAS_VALIDACAO", nullable = true)
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

