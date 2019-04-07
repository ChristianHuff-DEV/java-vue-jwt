package io.betweendata.auth.token;

import java.util.Optional;

import io.betweendata.auth.service.ServiceLocator;
import io.betweendata.auth.service.TokenService;
import io.betweendata.auth.user.InMemoryUserStorage;
import io.betweendata.auth.user.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * TokenAuthenticator is responsible to authenticate a user based on the
 * provided {@link TokenCredentials}.
 */
public class TokenAuthenticator implements Authenticator<TokenCredentials, User> {

    /**
     * Authenticate the given {@link TokenCredentials}.<br>
     * If authenticate return a user in it's Optional the authentication was
     * successful. If the Optional is empty it could mean that the token was invalid
     * or no user with the given mail exists.
     */
    @Override
    public Optional<User> authenticate(TokenCredentials credentials) throws AuthenticationException {
	TokenService tokenService = ServiceLocator.get().getTokenService();

	// Check if the token is valid (signed with our key)
	boolean isTokenValid = tokenService.isValid(credentials.getToken());

	if (!isTokenValid) {
	    // Return empty Optional if the token is invalid
	    return Optional.empty();
	}

	// Extract the email from the claims of the token
	String email = tokenService.extractClaimEmail(credentials.getToken());

	// Load the user using the mail address
	User user = InMemoryUserStorage.getInstance().loadUser(email);

	// Return empty optional if no user was found
	if (user == null) {
	    return Optional.empty();
	}

	user.setRole("USER"); // Default role
	user.setEmail(email);

	// If we get here the provided token is valid and a user for it was found in the
	// database.
	return Optional.ofNullable(user);
    }
}
