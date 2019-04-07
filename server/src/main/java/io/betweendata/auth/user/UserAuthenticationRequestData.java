package io.betweendata.auth.user;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data that has to be provided for login/register requests.
 * 
 * @author christian
 *
 */
public class UserAuthenticationRequestData {
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;

    public UserAuthenticationRequestData() {
	// Jackson constructor
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

}
