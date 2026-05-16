package org.femass.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.femass.dto.ErrorResponseDTO;
import org.femass.service.DadosFormularioService;

@ApplicationScoped
@Path("/dados-formulario")
public class DadosFormularioResource {

    @Inject
    DadosFormularioService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarDadosFormulario() {
        try {
            return Response.ok(service.buscarDadosFormulario()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Erro ao buscar dados do formulario"))
                    .build();
        }
    }
}
