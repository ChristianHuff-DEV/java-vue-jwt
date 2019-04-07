package io.betweendata.auth;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.betweendata.auth.service.ServiceLocator;
import io.betweendata.auth.token.TokenAuthFilter;
import io.betweendata.auth.user.ExampleHealthCheck;
import io.betweendata.auth.user.UserAuthenticationResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.logging.Logger;

/**
 * The main class in which the app is being configured and all resources (API endpoints) are
 * being registered.
 */
public class AuthenticationServiceApplication
        extends Application<AuthenticationServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new AuthenticationServiceApplication().run(args);
    }

    /**
     * Entry point of the app.
     *
     * @param configuration
     * @param environment
     * @throws Exception
     */
    @Override
    public void run(AuthenticationServiceConfiguration configuration, Environment environment)
            throws Exception {
        configureCors(environment);
        // If this feature is not disabled the JSON generated from a LocalDateTime
        // object will be an array instead of a string
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Register authentication filter
        environment.jersey().register(new AuthDynamicFeature(new TokenAuthFilter()));
        // Allow for role based authentication using Annotations
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        registerResources(environment);
        registerHealthChecks(environment);

        // Initialize services
        ServiceLocator.get().init(configuration);
    }

    /**
     * Registers all resources (API endpoint) provided by this app.
     *
     * @param environment
     */
    protected void registerResources(Environment environment) {
        UserAuthenticationResource authenticationResource = new UserAuthenticationResource();
        environment.jersey().register(authenticationResource);

    }

    /**
     * Register custom health checks.
     *
     * @param environment
     */
    protected void registerHealthChecks(Environment environment) {
        ExampleHealthCheck authenticationHealthCheck =
                new ExampleHealthCheck();
        environment.healthChecks().register("example", authenticationHealthCheck);
    }


    /**
     * Configure CORS
     *
     * @param environment
     */
    protected void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
                "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }

}
