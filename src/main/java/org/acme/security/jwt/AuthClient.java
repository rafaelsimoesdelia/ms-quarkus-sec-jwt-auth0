package org.acme.security.jwt;

import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri = "https://Seu_Domínio_Auth0")
@Path("/oauth/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthClient {

    @POST
    Uni<AuthResponse> login(Map<String, Object> request);
}