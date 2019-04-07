package io.betweendata.auth.service;

import java.security.InvalidParameterException;

import io.betweendata.auth.AuthenticationServiceConfiguration;

/**
 * Singleton implementation managing all services.<br>
 * Upon starting the application the
 * {@link ServiceLocator#init(AuthenticationServiceConfiguration)} method has to
 * be called before the first access to one of the service.
 */
public class ServiceLocator implements Services {

    private static final ServiceLocator instance = new ServiceLocator();

    private AuthenticationServiceConfiguration config;

    private PasswordService passwordService;
    private TokenService tokenService;


    private ServiceLocator() {
    }

    /**
     * Initializes this service locator.
     *
     * @param config
     */
    public void init(AuthenticationServiceConfiguration config) {
        if (config == null) {
            throw new InvalidParameterException("Config can't be null");
        }
        this.config = config;
        passwordService = new PasswordService();
        tokenService = new TokenService(config);
    }

    public static ServiceLocator get() {
        return instance;
    }

    @Override
    public PasswordService getPasswordService() {
        return passwordService;
    }

    @Override
    public TokenService getTokenService() {
        return tokenService;
    }

}
