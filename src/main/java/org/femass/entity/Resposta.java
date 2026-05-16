package org.femass.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respostas")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao; // Avaliação associada à resposta

    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta; // Pergunta respondida

    @Column(nullable = false)
    private Integer nota; // Resposta à pergunta (1 a 5)

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}