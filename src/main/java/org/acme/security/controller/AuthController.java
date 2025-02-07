package org.acme.security.controller;

import java.util.Map;

import org.acme.security.jwt.AuthRequest;
import org.acme.security.jwt.AuthService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador responsável por gerenciar autenticação e renovação de tokens.
 * Fornece endpoints para login e atualização do token JWT.
 *
 * @author rsdelia
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Endpoints de autenticação e geração de tokens")
public class AuthController {

    @Inject
    AuthService authService;

    /**
     * Endpoint para autenticação do usuário.
     *
     * @param authRequest Objeto contendo as credenciais do usuário (usuário e
     *                    senha).
     * @return Um {@link Uni<Response>} contendo o token JWT em caso de sucesso, ou
     *         {@code 401 Unauthorized} se a autenticação falhar.
     */
    @POST
    @Path("/login")
    @Operation(summary = "Autentica um usuário", description = "Gera um token JWT caso as credenciais estejam corretas.")
    @APIResponse(responseCode = "200", description = "Token JWT gerado com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "401", description = "Falha na autenticação")
    public Uni<Response> login(AuthRequest authRequest) {
	return authService.login(authRequest.username, authRequest.password).onItem()
		.transform(authResponse -> Response.ok(authResponse).build()).onFailure()
		.recoverWithItem(e -> Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
    }

    /**
     * Endpoint para atualização do token JWT utilizando um refresh token.
     *
     * @param request Um mapa contendo o refresh token na chave "refresh_token".
     * @return Um {@link Uni<Response>} contendo um novo token JWT em caso de
     *         sucesso, ou {@code 400 Bad Request} se o refresh token não for
     *         fornecido, ou {@code 401 Unauthorized} se o refresh token for
     *         inválido ou expirado.
     */
    @POST
    @Path("/refresh")
    @Operation(summary = "Atualiza o token JWT", description = "Gera um novo token a partir de um refresh token válido.")
    @APIResponse(responseCode = "200", description = "Novo token JWT gerado com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "400", description = "Refresh token não fornecido")
    @APIResponse(responseCode = "401", description = "Refresh token inválido")
    public Uni<Response> refreshToken(Map<String, String> request) {
	String refreshToken = request.get("refresh_token");
	if (refreshToken == null || refreshToken.isEmpty()) {
	    return Uni.createFrom()
		    .item(Response.status(Response.Status.BAD_REQUEST).entity("Refresh token is required").build());
	}

	return authService.refreshToken(refreshToken).onItem()
		.transform(authResponse -> Response.ok(authResponse).build()).onFailure()
		.recoverWithItem(e -> Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build());
    }
}
