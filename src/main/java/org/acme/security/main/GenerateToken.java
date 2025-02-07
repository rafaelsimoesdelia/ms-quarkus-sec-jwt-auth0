package org.acme.security.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * A simple utility class to generate and print a JWT token string to stdout.
 */
public class GenerateToken {
    /**
     * Generate JWT token
     */
    public static void main(String[] args) {
	try {
	    // Criando o JSON do corpo da requisição
	    String jsonBody = """
	    	    {
	    	        "client_id": "client_id_dicas",
	    	        "client_secret": "client_secret_dicas",
	    	        "audience": "audience-my-api",
	    	        "grant_type": "password",
	    	        "username": "finhat2@gmail.com",
	    	        "password": "password_dicas",
	    	        "scope": "openid profile email offline_access"
	    	    }
	    	""";

	    // Criando o cliente HTTP
	    HttpClient client = HttpClient.newHttpClient();

	    Config config = ConfigProvider.getConfig();
	    String tokenUrl = config.getValue("auth0.token-url", String.class);

	    // Criando a requisição HTTP POST
	    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(tokenUrl))
		    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonBody))
		    .build();

	    // Enviando a requisição e obtendo a resposta
	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	    // Exibindo a resposta no console
	    System.out.println("Status Code: " + response.statusCode());
	    System.out.println("Response Body: " + response.body());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}