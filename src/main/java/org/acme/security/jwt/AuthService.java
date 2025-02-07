package org.acme.security.jwt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "auth0.client-id")
    String clientId;

    @ConfigProperty(name = "auth0.client-secret")
    String clientSecret;

    @ConfigProperty(name = "auth0.audience")
    String audience;

    @Inject
    @RestClient
    AuthClient authClient;

    public Uni<AuthResponse> login(String username, String password) {
	Map<String, Object> payload = new HashMap<>();
	payload.put("client_id", clientId);
	payload.put("client_secret", clientSecret);
	payload.put("audience", audience);
	payload.put("grant_type", "password");
	payload.put("username", username);
	payload.put("password", password);
	payload.put("scope", "openid profile email offline_access");

	return authClient.login(payload).onItem().transform(response -> response).onFailure().recoverWithItem(e -> {
	    throw new RuntimeException("Erro na autenticação: " + e.getMessage());
	});
    }

    public Uni<AuthResponse> refreshToken(String refreshToken) {
	Map<String, Object> payload = new HashMap<>();
	payload.put("client_id", clientId);
	payload.put("client_secret", clientSecret);
	payload.put("grant_type", "refresh_token");
	payload.put("refresh_token", refreshToken);

	return authClient.login(payload).onItem().transform(response -> response).onFailure().recoverWithItem(e -> {
	    throw new RuntimeException("Erro na renovação do token: " + e.getMessage());
	});
    }
}