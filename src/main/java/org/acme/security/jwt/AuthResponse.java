package org.acme.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonIgnore
    @JsonProperty("id_token")
    public String idToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonIgnore
    @JsonProperty("expires_in")
    public int expiresIn;

    @JsonIgnore
    @JsonProperty("token_type")
    public String tokenType;

    @JsonIgnore
    @JsonProperty("scope")
    public String scope;
}
