package org.femass.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.femass.dto.ErrorResponseDTO;
import org.femass.dto.FormularioDTO;
import org.femass.exception.CPFInvalidoException;
import org.femass.exception.InvalidFormularioException;
import org.femass.service.FormularioService;

@ApplicationScoped
@Path("/formulario")
public class FormularioResource {

    @Inject
    private FormularioService formularioService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response PostFormulario(FormularioDTO formularioDTO) {
        try {
            formularioService.salvar(formularioDTO);
            return Response.ok().build();
        } catch (CPFInvalidoException | InvalidFormularioException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
        }
    }

}
