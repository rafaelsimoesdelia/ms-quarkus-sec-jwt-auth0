package org.acme.security.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Controlador responsável por gerenciar endpoints protegidos por autenticação e
 * autorização. Os acessos são controlados via JWT e roles definidas no Auth0.
 *
 * @author rsdelia
 */
@Path("/protected")
public class ProtectedController {

    /**
     * Endpoint acessível apenas por usuários com a role "admin".
     *
     * @return String indicando o acesso permitido.
     */
    @GET
    @Path("/endpoint1")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin")
    public String endpoint1() {
	return "Acesso permitido ao Endpoint 1 (Apenas Admin)";
    }

    /**
     * Endpoint acessível por usuários com as roles "admin" e "user_monster".
     *
     * @return String indicando o acesso permitido.
     */
    @GET
    @Path("/endpoint2")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({ "admin", "user_monster" })
    public String endpoint2() {
	return "Acesso permitido ao Endpoint 2 (Admin e User Monster)";
    }

    /**
     * Endpoint acessível apenas por usuários com a role "user_monster".
     *
     * @return String indicando o acesso permitido.
     */
    @GET
    @Path("/endpoint3")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user_monster")
    public String endpoint3() {
	return "Acesso permitido ao Endpoint 3 (Apenas User Monster)";
    }
}