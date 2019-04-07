package io.betweendata.auth;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * Class representing the (custom) configuration.<br>
 * This class represents the implementation of the <i>config.yml</i>.
 */
public class AuthenticationServiceConfiguration extends Configuration {

    @NotEmpty
    private String tokenSecret;

    @JsonProperty
    public String getTokenSecret() {
	return tokenSecret;
    }

    @JsonProperty
    public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
    }

}
