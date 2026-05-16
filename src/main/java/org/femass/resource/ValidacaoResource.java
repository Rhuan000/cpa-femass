package org.femass.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.femass.dto.ErrorResponseDTO;
import org.femass.dto.ValidacaoDetailResponseDTO;
import org.femass.dto.ValidacaoResponseDTO;
import org.femass.dto.ValidacaoStatusDTO;
import org.femass.entity.Validacao;
import org.femass.service.ValidacaoService;

@ApplicationScoped
@Path("/validacao")
public class ValidacaoResource {

    @Inject
    ValidacaoService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Path("/armazenar-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response armazenarHash(String payload) {
        Validacao validacao = service.armazenarHash(payload);
        ValidacaoResponseDTO response = new ValidacaoResponseDTO(
            "Payload recebido com sucesso!",
            validacao.id,
            validacao.getHash(),
            payload
        );
        return Response.ok().entity(response).build();
    }

    @PUT
    @Path("/validar-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarHash(@QueryParam("hash") String hash) {
        if (hash == null || hash.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseDTO("Hash é obrigatório como parâmetro de query"))
                .build();
        }


        try {
            Validacao validacao = service.validarHash(hash);
            ValidacaoResponseDTO response = new ValidacaoResponseDTO(
                "Hash validado com sucesso!",
                validacao.id,
                validacao.getHash(),
                "validacao_confirmada"
            );
            return Response.ok().entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponseDTO(e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO("Erro ao validar hash: " + e.getMessage()))
                .build();
        }
    }

    @PUT
    @Path("/validar-hash/detalhado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarHashDetalhado(@QueryParam("hash") String hash) {
        if (hash == null || hash.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO("Hash é obrigatório como parâmetro de query"))
                    .build();
        }

        try {
            Validacao validacao = service.validarHash(hash);
            long tempoDecorrido = java.time.temporal.ChronoUnit.MILLIS.between(
                validacao.getDataCriacao(),
                validacao.getDataValidacao()
            );

            ValidacaoDetailResponseDTO response = new ValidacaoDetailResponseDTO(
                true,
                validacao.id,
                validacao.getHash(),
                validacao.getValidado(),
                validacao.getDataCriacao(),
                validacao.getDataValidacao(),
                validacao.getTentativasValidacao(),
                tempoDecorrido
            );
            return Response.ok().entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponseDTO(e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO("Erro ao validar hash: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GET
    @Path("/verificar-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarStatus(@QueryParam("hash") String hash) {
        if (hash == null || hash.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseDTO("Hash é obrigatório como parâmetro de query"))
                .build();
        }

        try {
            Boolean validado = service.verificarStatusValidacao(hash);
            String status = validado ? "VALIDADO" : "PENDENTE";
            String mensagem = validado
                ? "Este hash foi validado com sucesso"
                : "Este hash ainda está pendente de validação";

            ValidacaoStatusDTO response = new ValidacaoStatusDTO(hash, validado, status, mensagem);
            return Response.ok().entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponseDTO(e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO("Erro ao verificar status: " + e.getMessage()))
                .build();
        }
    }

    @GET
    @Path("/buscar-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarHash(@QueryParam("hash") String hash) {
        if (hash == null || hash.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseDTO("Hash é obrigatório como parâmetro de query"))
                .build();
        }

        try {
            Validacao validacao = service.buscarHash(hash);
            ValidacaoResponseDTO response = new ValidacaoResponseDTO(
                "Hash encontrado com sucesso!",
                validacao.id,
                validacao.getHash(),
                validacao.getValidado() ? "validado" : "pendente"
            );
            return Response.ok().entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponseDTO(e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDTO("Erro ao buscar hash: " + e.getMessage()))
                .build();
        }
    }
}
