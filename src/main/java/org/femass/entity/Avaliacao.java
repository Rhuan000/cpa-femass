package org.femass.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Usuário que fez a avaliação

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina; // Disciplina avaliada

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas; // Respostas das perguntas

    @Column(name = "comentarios", length = 1000)
    private String comentariosGerais; // Comentários adicionais

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public String getComentariosGerais() {
        return comentariosGerais;
    }

    public void setComentariosGerais(String comentariosGerais) {
        this.comentariosGerais = comentariosGerais;
    }
}