package org.femass.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.femass.dto.FormularioDTO;
import org.femass.service.FormularioService;

import java.awt.*;

@ApplicationScoped
@Path("/formulario")
public class FormularioResource {

    @Inject
    private FormularioService formularioService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response PostFormulario(FormularioDTO formularioDTO) {
        return Response.ok(formularioDTO).build()  ;
    }

}
