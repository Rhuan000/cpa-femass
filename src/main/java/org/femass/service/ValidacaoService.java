package org.femass.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.femass.entity.Validacao;

@ApplicationScoped
public class ValidacaoService {

    /**
     * Armazena o hash de um payload na entidade Validacao
     * @param payload - o payload a ser armazenado como hash
     * @return a entidade Validacao persistida
     */
    @Transactional
    public Validacao armazenarHash(String payload) {
        // Gera um hash SHA-256 do payload
        String hash = gerarHashSHA256(payload);
        
        // Se o hash já existir, retorna a entidade existente para evitar violação de unique
        Validacao existente = Validacao.find("hash", hash).firstResult();
        if (existente != null) {
            System.out.println("Hash já existente encontrado: " + hash);
            return existente;
        }

        // Cria e persiste a entidade Validacao
        Validacao validacao = new Validacao();
        validacao.setHash(hash);
        validacao.setValidado(false);
        try {
            validacao.persist();
            System.out.println("Hash armazenado com sucesso: " + hash);
            return validacao;
        } catch (jakarta.persistence.PersistenceException pe) {
            // Pode ocorrer uma condição de corrida: outro processo inseriu o mesmo hash.
            // Recupera e retorna o registro existente.
            Validacao recuperado = Validacao.find("hash", hash).firstResult();
            if (recuperado != null) {
                System.out.println("Persistência falhou por duplicate key; retornando registro existente para hash: " + hash);
                return recuperado;
            }
            throw pe;
        }
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
