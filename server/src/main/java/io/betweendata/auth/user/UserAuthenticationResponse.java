package io.betweendata.auth.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.betweendata.auth.service.ServiceLocator;
import io.betweendata.auth.service.TokenService;
import io.betweendata.auth.token.InMemoryRefreshTokenCache;

/**
 * Response to an authentication request (login/register) containing all info
 * for the client to know the authentication state.
 * 
 * @author christian
 *
 */
public class UserAuthenticationResponse {
    private String email;
    private String accessToken;
    /**
     * The point in time when the access token expires.
     */
    private LocalDateTime expiresAt;
    private String refreshToken;

    public UserAuthenticationResponse() {
	// Empty constructor for Jackson
    }

    public UserAuthenticationResponse(String email, String accessToken, String refreshToken, LocalDateTime expiresAt) {
	super();
	this.email = email;
	this.accessToken = accessToken;
	this.refreshToken = refreshToken;
	this.expiresAt = expiresAt;
    }

    @JsonProperty
    public String getEmail() {
	return email;
    }

    @JsonProperty
    public String getAccessToken() {
	return accessToken;
    }

    @JsonProperty
    public String getRefreshToken() {
	return refreshToken;
    }

    @JsonProperty
    public LocalDateTime getExpiresAt() {
	return expiresAt;
    }

    /**
     * Convenience method to create an {@link UserAuthenticationResponse}.<br>
     * This includes creating a refresh/access token and adding the refresh token to
     * our cache of valid tokens that can be used to refresh an access token.
     * 
     * @param email
     * @return
     */
    public static UserAuthenticationResponse create(String email) {

	TokenService tokenService = ServiceLocator.get().getTokenService();

	// Create access token
	String accessToken = tokenService.createAccessToken(email);

	// Create refresh token
	String refreshToken = tokenService.createRefreshToken(email);

	// Remember the refresh token in the cache
	InMemoryRefreshTokenCache.getInstance().add(refreshToken);

	return new UserAuthenticationResponse(email, accessToken, refreshToken,
		tokenService.getClaimExpiresAt(accessToken));
    }
}
