package io.betweendata.auth.token;

/**
 * Class representing the credentials that have to be provided for a request to
 * be authenticated.
 * 
 * @author christian
 *
 */
public class TokenCredentials {
    private String token;

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }
}
