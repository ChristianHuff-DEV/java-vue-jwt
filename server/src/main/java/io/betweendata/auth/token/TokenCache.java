package io.betweendata.auth.token;

/**
 * Interface describing which methods the cache for tokens has to implement.
 * 
 * @author christian
 *
 */
public interface TokenCache {
    /**
     * Add new token to the cache.
     *
     * @param token
     */
    void add(String token);

    /**
     * Remove the given token from the cache.
     *
     * @param token
     */
    void remove(String token);

    /**
     * Check if the given token is cached.
     *
     * @param token
     * @return
     */
    boolean exists(String token);
}
