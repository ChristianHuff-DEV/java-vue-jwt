package io.betweendata.auth.service;

/**
 * Services is an gives an overview of all available {@link Service}s. Each service needs to
 * implement a get method here so {@link ServiceLocator} is aware of which services are available.
 */
public interface Services {

    PasswordService getPasswordService();

    TokenService getTokenService();

}
