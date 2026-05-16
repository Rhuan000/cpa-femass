package org.femass.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.femass.entity.Validacao;
import java.time.LocalDateTime;

@ApplicationScoped
public class ValidacaoService {

    /**
     * Armazena o hash de um payload na entidade Validacao
     * @param payload - o payload a ser armazenado como hash
     * @return a entidade Validacao persistida
     */
    @Transactional
    public Validacao armazenarHash(String payload) {
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Payload e obrigatorio para gerar o hash");
        }

        // Gera um hash SHA-256 do payload
        String hash = gerarHashSHA256(payload);

        Validacao validacaoExistente = Validacao.find("hash", hash).firstResult();
        if (validacaoExistente != null) {
            return validacaoExistente;
        }
        
        // Cria e persiste a entidade Validacao
        Validacao validacao = new Validacao();
        validacao.setHash(hash);
        validacao.setValidado(false);
        validacao.persist();
        
        System.out.println("Hash armazenado com sucesso: " + hash);
        return validacao;
    }

    /**
     * Valida um hash existente no banco de dados
     * Incrementa tentativas de validação e marca data/hora
     * @param hash - o hash a ser validado
     * @return a entidade Validacao com status atualizado
     * @throws IllegalArgumentException se o hash não existir
     */
    @Transactional
    public Validacao validarHash(String hash) {
        Validacao validacao = Validacao.find("hash", hash).firstResult();
        
        if (validacao == null) {
            throw new IllegalArgumentException("Hash não encontrado no banco de dados");
        }
        
        // Incrementa tentativas de validação
        if (validacao.getTentativasValidacao() == null) {
            validacao.setTentativasValidacao(0);
        }
        validacao.setTentativasValidacao(validacao.getTentativasValidacao() + 1);
        
        // Marca como validado
        validacao.setValidado(true);



        validacao.setDataValidacao(java.time.LocalDateTime.now());
        
        System.out.println("Hash validado com sucesso: " + hash);
        return validacao;
    }

    /**
     * Valida um hash e retorna informações detalhadas
     * @param hash - o hash a ser validado
     * @return informações de validação (sucesso, tempo decorrido, tentativas)
     * @throws IllegalArgumentException se o hash não existir
     */
    public java.util.Map<String, Object> validarHashComDetalhes(String hash) {
        Validacao validacao = validarHash(hash);
        
        long tempoDecorridoMs = 0;
        if (validacao.getDataCriacao() != null && validacao.getDataValidacao() != null) {
            tempoDecorridoMs = java.time.temporal.ChronoUnit.MILLIS.between(
                validacao.getDataCriacao(), 
                validacao.getDataValidacao()
            );
        }
        
        java.util.Map<String, Object> detalhes = new java.util.HashMap<>();
        detalhes.put("hashValido", true);
        detalhes.put("hash", validacao.getHash());
        detalhes.put("validado", validacao.getValidado());
        detalhes.put("dataCriacao", validacao.getDataCriacao());
        detalhes.put("dataValidacao", validacao.getDataValidacao());
        detalhes.put("tentativasValidacao", validacao.getTentativasValidacao());
        detalhes.put("tempoDecorridoMs", tempoDecorridoMs);
        
        return detalhes;
    }

    /**
     * Verifica o status de validação de um hash
     * @param hash - o hash a ser verificado
     * @return true se validado, false se pendente
     * @throws IllegalArgumentException se o hash não existir
     */
    public Boolean verificarStatusValidacao(String hash) {
        Validacao validacao = Validacao.find("hash", hash).firstResult();
        
        if (validacao == null) {
            throw new IllegalArgumentException("Hash não encontrado no banco de dados");
        }
        
        return validacao.getValidado();
    }

    /**
     * Busca um hash no banco de dados sem modificar
     * @param hash - o hash a ser procurado
     * @return a entidade Validacao se encontrada
     * @throws IllegalArgumentException se o hash não existir
     */
    public Validacao buscarHash(String hash) {
        Validacao validacao = Validacao.find("hash", hash).firstResult();
        
        if (validacao == null) {
            throw new IllegalArgumentException("Hash não encontrado no banco de dados");
        }
        
        return validacao;
    }

    /**
     * Gera um hash SHA-256 de um payload
     * @param payload - o texto a ser convertido em hash
     * @return hash SHA-256 em formato hexadecimal
     */
    private String gerarHashSHA256(String payload) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256: " + e.getMessage(), e);
        }
    }
}
