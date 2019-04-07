package io.betweendata.auth.user;

import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.codahale.metrics.annotation.Timed;

import io.betweendata.auth.service.PasswordService;
import io.betweendata.auth.service.ServiceLocator;
import io.betweendata.auth.service.TokenService;
import io.betweendata.auth.token.InMemoryRefreshTokenCache;
import io.betweendata.auth.token.RefreshTokenRequestData;

/**
 * Definition of the end-points provided by this service.
 * 
 * @author christian
 *
 */
@Path("/")
public class UserAuthenticationResource {
    private static final Logger LOG = Logger.getLogger(UserAuthenticationResource.class.getName());

    public UserAuthenticationResource() {

    }

    /**
     * End-point to refresh an access token using a refresh token.
     * 
     * @param requestData
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @Path("/refresh")
    public UserAuthenticationResponse refresh(@NotNull RefreshTokenRequestData requestData) {

	// Check if we "know" the refresh token
	if (!InMemoryRefreshTokenCache.getInstance().exists(requestData.getRefreshToken())) {
	    // If we don't know the refresh token we will not issue an new access token
	    throw new WebApplicationException(Status.FORBIDDEN);
	}

	TokenService tokenService = ServiceLocator.get().getTokenService();
	String emailFromToken = tokenService.extractClaimEmail(requestData.getRefreshToken());

	// Issue a new refresh token
	String accessToken = ServiceLocator.get().getTokenService().createAccessToken(emailFromToken);

	return new UserAuthenticationResponse(emailFromToken, accessToken, requestData.getRefreshToken(),
		ServiceLocator.get().getTokenService().getClaimExpiresAt(accessToken));
    }

    /**
     * End-point allowing a user to log in.
     * 
     * @param requestData
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @Path("/login")
    public UserAuthenticationResponse login(@NotNull UserAuthenticationRequestData requestData) {

	// Load the user (if it exists)
	User user = InMemoryUserStorage.getInstance().loadUser(requestData.getEmail());

	if (user == null) {
	    throw new WebApplicationException(Status.UNAUTHORIZED);
	}

	// Hash the password
	try {
	    if (!ServiceLocator.get().getPasswordService().verifyPassword(requestData.getPassword(),
		    user.getPasswordHash())) {
		throw new WebApplicationException(Status.UNAUTHORIZED);
	    }
	} catch (PasswordService.CannotPerformOperationException | PasswordService.InvalidHashException e) {
	    throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
	}

	// If we get there the user exists and has entered the correct password
	// We can now create the response containing the tokens authenticating the user
	// is subsequent requests
	return UserAuthenticationResponse.create(user.getEmail());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @Path("/register")
    public UserAuthenticationResponse register(
	    @NotNull @Valid UserAuthenticationRequestData userAuthenticationRequestData) {
	User user = new User();

	// Validate email address
	if (!UserAuthenticationValidator.isEmailValid(userAuthenticationRequestData.getEmail())) {
	    throw new WebApplicationException("Invalid email", Status.BAD_REQUEST);
	}
	user.setEmail(userAuthenticationRequestData.getEmail());

	// Validate the password
	if (!UserAuthenticationValidator.isPasswordValid(userAuthenticationRequestData.getPassword())) {
	    throw new WebApplicationException("Invalid password", Status.BAD_REQUEST);
	}

	// Hash the password
	try {
	    String hashedPassword = ServiceLocator.get().getPasswordService()
		    .createHash(userAuthenticationRequestData.getPassword());
	    user.setPasswordHash(hashedPassword);
	} catch (PasswordService.CannotPerformOperationException e) {
	    throw new WebApplicationException(Status.BAD_REQUEST);
	}

	UserStorage userStorage = InMemoryUserStorage.getInstance();

	// Check that the email is not already registered
	if (userStorage.loadUser(userAuthenticationRequestData.getEmail()) != null) {
	    throw new WebApplicationException("Email already registered", Status.BAD_REQUEST);
	}

	// Save the new user
	userStorage.saveUser(user);

	return UserAuthenticationResponse.create(user.getEmail());
    }

    @RolesAllowed({ "USER" })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @Path("/user")
    public User user(@Context SecurityContext context) {
	// Get the user (principal) from the security context. This will be added there
	// by the TokenAuthFilter
	User user = (User) context.getUserPrincipal();

	// Remove the password hash. There is no need to send it around the internet...
	user.setPasswordHash(null);

	return user;
    }
}
