package org.femass.dto;

import java.time.LocalDateTime;

public class ValidacaoDetailResponseDTO {
	private Boolean hashValido;
	private Long id;
	private String hash;
	private Boolean validado;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataValidacao;
	private Integer tentativasValidacao;
	private Long tempoDecorridoMs;

	public ValidacaoDetailResponseDTO(Boolean hashValido, Long id, String hash, Boolean validado,
									  LocalDateTime dataCriacao, LocalDateTime dataValidacao,
									  Integer tentativasValidacao, Long tempoDecorridoMs) {
		this.hashValido = hashValido;
		this.id = id;
		this.hash = hash;
		this.validado = validado;
		this.dataCriacao = dataCriacao;
		this.dataValidacao = dataValidacao;
		this.tentativasValidacao = tentativasValidacao;
		this.tempoDecorridoMs = tempoDecorridoMs;
	}

	public Boolean getHashValido() { return hashValido; }
	public void setHashValido(Boolean hashValido) { this.hashValido = hashValido; }
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getHash() { return hash; }
	public void setHash(String hash) { this.hash = hash; }
	public Boolean getValidado() { return validado; }
	public void setValidado(Boolean validado) { this.validado = validado; }
	public LocalDateTime getDataCriacao() { return dataCriacao; }
	public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
	public LocalDateTime getDataValidacao() { return dataValidacao; }
	public void setDataValidacao(LocalDateTime dataValidacao) { this.dataValidacao = dataValidacao; }
	public Integer getTentativasValidacao() { return tentativasValidacao; }
	public void setTentativasValidacao(Integer tentativasValidacao) { this.tentativasValidacao = tentativasValidacao; }
	public Long getTempoDecorridoMs() { return tempoDecorridoMs; }
	public void setTempoDecorridoMs(Long tempoDecorridoMs) { this.tempoDecorridoMs = tempoDecorridoMs; }
}

