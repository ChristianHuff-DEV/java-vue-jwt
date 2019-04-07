package io.betweendata.auth.user;

import java.security.Principal;

/**
 * Representation of a user. This is also used in the context of authentication.
 * 
 * @author christian
 *
 */
public class User implements Principal {
    private String email;
    private String passwordHash;
    private String role;

    public User() {
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPasswordHash() {
	return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    @Override
    public String getName() {
	return email;
    }
}
