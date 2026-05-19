package org.femass.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.femass.dto.FormularioDTO;
import org.femass.dto.QRCodePayloadDTO;
import org.femass.dto.SubjectDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class QRCodeService {

    @Inject
    ObjectMapper objectMapper;

    public QRCodePayloadDTO criarPayload(FormularioDTO formularioDTO) {
        return new QRCodePayloadDTO(
                primeirosQuatroDigitosCpf(formularioDTO.respondent.cpf),
                formularioDTO.respondent.matricula,
                formularioDTO.respondent.aceiteTermosCondicoesServico,
                List.of(formularioDTO.course.name),
                //gerarCodigosDisciplinas(formularioDTO.subjects),
                formularioDTO.subjects.stream()
                        .map(subject -> subject.subjectName)
                        .toList(),
                //UUID.randomUUID().toString()
                "97db36ef9907c55d069dee4558269a42"
        );
    }

    public String codificar(QRCodePayloadDTO payload) {
        validarPayload(payload);

        try {
            String json = objectMapper.writeValueAsString(payload);
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erro ao codificar payload do QR Code", e);
        }
    }

    public QRCodePayloadDTO decodificar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Codigo do QR Code e obrigatorio");
        }

        try {
            byte[] jsonBytes = Base64.getUrlDecoder().decode(codigo);
            return objectMapper.readValue(jsonBytes, QRCodePayloadDTO.class);
        } catch (IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException("Codigo do QR Code invalido");
        }
    }

    private void validarPayload(QRCodePayloadDTO payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload do QR Code e obrigatorio");
        }

        if (payload.cpf == null || payload.cpf.isBlank()) {
            throw new IllegalArgumentException("CPF parcial e obrigatorio");
        }

        if (payload.matricula == null || payload.matricula.isBlank()) {
            throw new IllegalArgumentException("Matricula e obrigatoria");
        }

        if (!Boolean.TRUE.equals(payload.aceiteTermosCondicoesServico)) {
            throw new IllegalArgumentException("Aceite dos termos e condicoes de servico e obrigatorio");
        }

        if (payload.cursos == null || payload.cursos.isEmpty()) {
            throw new IllegalArgumentException("Ao menos um curso e obrigatorio");
        }

        if (payload.disciplinas == null || payload.disciplinas.isEmpty()) {
            throw new IllegalArgumentException("Ao menos uma disciplina e obrigatoria");
        }

        if (payload.identificador == null || payload.identificador.isBlank()) {
            throw new IllegalArgumentException("Identificador e obrigatorio");
        }
    }

    private List<String> gerarCodigosDisciplinas(List<SubjectDTO> subjects) {
        return subjects.stream()
                .map(subject -> tresPrimeirasLetras(subject.subjectName))
                .toList();
    }

    private String primeirosQuatroDigitosCpf(String cpf) {
        return apenasDigitos(cpf).substring(0, 4);
    }

    private String apenasDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }

    private String tresPrimeirasLetras(String valor) {
        String normalizado = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase();

        if (normalizado.length() < 3) {
            throw new IllegalArgumentException("Nome da disciplina deve ter ao menos 3 letras");
        }

        return normalizado.substring(0, 3);
    }
}
