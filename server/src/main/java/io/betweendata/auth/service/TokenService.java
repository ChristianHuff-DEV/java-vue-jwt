package io.betweendata.auth.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.crypto.SecretKey;

import io.betweendata.auth.AuthenticationServiceConfiguration;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service helping to create tokens, validate them and extract information.
 */
public class TokenService implements Service {

    /**
     * Amount of minutes an access token expires in.
     */
    private final static int EXPIRES_AT_ACCESS_TOKEN_MINUTES = 5;
    /**
     * Amount of days an refresh token expires in.
     */
    private final static int EXPIRES_AT_REFRESH_TOKEN_DAYS = 5;


    /**
     * Key is used to sign all tokens.
     */
    private SecretKey key;

    public TokenService(AuthenticationServiceConfiguration config) {
	key = Keys.hmacShaKeyFor(config.getTokenSecret().getBytes());
    }

    /**
     * Create a short lived access token.
     * @param email
     * @return
     */
    public String createAccessToken(String email) {
        return createBaseTokenBuilder(email)
                .setSubject("AccessToken")
                .setExpiration(getDefaultExpiresAtAccessToken())
                .compact();
    }

    /**
     * Create a long lived refresh token.
     * @param email
     * @return
     */
    public String createRefreshToken(String email) {
        return createBaseTokenBuilder(email)
                .setSubject("RefreshToken")
                .setExpiration(getDefaultExpiresAtRefreshToken())
                .compact();
    }

    /**
     * Creates a base token with all information that is shared between all tokens. Methods
     * calling this method only have to make their individual adjustments (i.e. adding the
     * expiration date / subject).
     *
     * @param email
     * @return
     */
    protected JwtBuilder createBaseTokenBuilder(String email) {
        return Jwts.builder()
                .signWith(key)
                .setExpiration(getDefaultExpiresAtRefreshToken())
                .claim("email", email);
    }

    /**
     * Checks whether or not the given token was signed with our key and therefore
     * can be trusted.
     * 
     * @param token
     * @return
     */
    public boolean isValid(String token) {
	try {
	    Jwts.parser().setSigningKey(key).parseClaimsJws(token);
	} catch (JwtException ex) {
	    return false;
	}
	return true;
    }

    /**
     * Returns the expires at date of the given token.
     *
     * @param token
     * @return
     */
    public LocalDateTime getClaimExpiresAt(String token) {
        Instant expiresAt = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .toInstant();
        return LocalDateTime.ofInstant(expiresAt, ZoneOffset.UTC);
    }

    /**
     * Return the email from the claims of the given token.
     * @param token
     * @return
     */
    public String extractClaimEmail(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody()
                .get("email", String.class);
    }

    /**
     * Returns the default date when an access token expires at from the moment the method has
     * been called.
     *
     * @return
     */
    public Date getDefaultExpiresAtAccessToken() {
        LocalDateTime now = LocalDateTime.now((ZoneOffset.UTC));

        LocalDateTime expiresAt = now.plusMinutes(EXPIRES_AT_ACCESS_TOKEN_MINUTES);

        return Date.from(expiresAt.toInstant(ZoneOffset.UTC));
    }

    /**
     * Returns the default date when an refresh token expires at from the moment the method has
     * been called.
     *
     * @return
     */
    public Date getDefaultExpiresAtRefreshToken() {
        LocalDateTime now = LocalDateTime.now((ZoneOffset.UTC));

        LocalDateTime expiresAt = now.plusDays(EXPIRES_AT_REFRESH_TOKEN_DAYS);

        return Date.from(expiresAt.toInstant(ZoneOffset.UTC));
    }

    /**
     * Takes an authorization header an returns the token part of it. <br>
     * The header is expected to be in the following format: "Bearer [TOKEN]". What this methods
     * basically does is remove the "Bearer " part.
     *
     * @param authHeader
     * @return the token part of the header
     * @throws IllegalArgumentException - if the header is malformed
     */
    public String parseAuthorizationHeader(String authHeader) throws IllegalArgumentException {
        // Check that the headers format is valid
        if (!authHeader.startsWith("Bearer ") && authHeader.length() < 7) {
            throw new IllegalArgumentException("Malformed authorization header");
        }
        // Extract the token part
        return authHeader.substring(authHeader.indexOf(" ") + 1);
    }
}
