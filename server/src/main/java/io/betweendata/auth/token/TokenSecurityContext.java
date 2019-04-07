package io.betweendata.auth.token;

import io.betweendata.auth.user.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Our implementation of {@link SecurityContext} giving us access to security
 * (authentication) related data in the context of an request.
 * 
 * @author christian
 *
 */
public class TokenSecurityContext implements SecurityContext {
    private User user;
    private SecurityContext securityContext;

    public TokenSecurityContext(User user, SecurityContext securityContext) {
	this.user = user;
	this.securityContext = securityContext;
    }

    @Override
    public Principal getUserPrincipal() {
	return user;
    }

    @Override
    public boolean isUserInRole(String role) {
	return role.equals(user.getRole());
    }

    @Override
    public boolean isSecure() {
	return securityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
	return "TOKEN";
    }
}
