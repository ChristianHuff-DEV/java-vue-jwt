package io.betweendata.auth.token;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache to keep track of valid refresh token. These token can be used to obtain a new access
 * token. Requests with unknown refresh token will be rejected.
 */
public class InMemoryRefreshTokenCache implements TokenCache {
    private static final InMemoryRefreshTokenCache instance = new InMemoryRefreshTokenCache();

    /**
     * Cache for token.<br>
     * <b>Key:</b> the cached token<br>
     * <b>Value:</b> empty string
     */
    private Map<String, String> cache = new HashMap<>();


    private InMemoryRefreshTokenCache() {
    }

    public static InMemoryRefreshTokenCache getInstance() {
        return instance;
    }

    @Override
    public void add(String token) {
        cache.put(token, "");
    }

    @Override
    public void remove(String token) {
        cache.remove(token);
    }

    @Override
    public boolean exists(String token) {
        return cache.containsKey(token);
    }
}
