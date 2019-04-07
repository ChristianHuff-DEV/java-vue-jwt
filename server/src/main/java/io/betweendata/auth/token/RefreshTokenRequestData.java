package io.betweendata.auth.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of the data that needs to be provided in the request to
 * refresh an access token.
 */
public class RefreshTokenRequestData {

    @JsonProperty("refresh_token")
    private String RefreshToken;

    public RefreshTokenRequestData() {
	// Jackson constructor
    }

    public String getRefreshToken() {
	return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
	RefreshToken = refreshToken;
    }
}
