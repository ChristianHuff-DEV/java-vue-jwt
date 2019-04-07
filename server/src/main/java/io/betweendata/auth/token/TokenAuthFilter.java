package io.betweendata.auth.token;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.betweendata.auth.service.ServiceLocator;
import io.betweendata.auth.service.TokenService;
import io.betweendata.auth.user.User;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;

/**
 * TokenAuthFilter has the following responsibilities:<br>
 * <ul>
 * <li>Extract the data needed to perform authentication</li>
 * <li>Delegate the authentication to
 * {@link TokenAuthenticator#authenticate(TokenCredentials)}</li>
 * <li>Handle the result of
 * {@link TokenAuthenticator#authenticate(TokenCredentials)}</li>
 * <li>Add the authentication information to the context to make it available to
 * the following process</li>
 * </ul>
 * 
 * 
 * @author christian
 *
 */
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthFilter extends AuthFilter<TokenCredentials, User> {
    private TokenAuthenticator authenticator = new TokenAuthenticator();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
	TokenService tokenService = ServiceLocator.get().getTokenService();

	// Extract header from request containing the token
	String authHeader = requestContext.getHeaders().getFirst("Authorization");

	// If the header is missing or empty we return an error
	if (authHeader == null || authHeader.isEmpty()) {
	    throw new WebApplicationException(Response.Status.FORBIDDEN);
	}

	// Extract the token from the header
	String token = tokenService.parseAuthorizationHeader(authHeader);

	// Create the credentials that will be used to authenticate the request
	TokenCredentials credentials = new TokenCredentials();
	credentials.setToken(token);

	// Create user optional. If this stays empty the authentication was
	// unsuccessful.
	Optional<User> user = Optional.empty();

	try {
	    // Delegate the authentication to our authenticator
	    user = authenticator.authenticate(credentials);
	} catch (AuthenticationException e) {
	    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}

	// If the user optional is empty the authentication was unsuccessful.
	if (user.isEmpty()) {
	    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}

	// If we get here the request was authenticated successfully.
	SecurityContext securityContext = new TokenSecurityContext(user.get(), requestContext.getSecurityContext());
	requestContext.setSecurityContext(securityContext);
    }

}
