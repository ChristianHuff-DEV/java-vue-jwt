package io.betweendata.auth.user;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory storage to manage user data.<br>
 * <b>Attention:</b> This implementation is only meant for testing and should
 * only be used for unit tests or development.
 * 
 * @author christian
 *
 */
public class InMemoryUserStorage implements UserStorage {
    private static final InMemoryUserStorage instance = new InMemoryUserStorage();

    private InMemoryUserStorage() {
    }

    private Map<String, User> users = new HashMap<String, User>();

    public static UserStorage getInstance() {
	return instance;
    }

    @Override
    public void saveUser(User user) {
	users.put(user.getEmail(), user);
    }

    /**
     * In this in-memory implementation the returned user is a copy of the object
     * stored in the map.<br>
     * This is due to the fact that the user we get via "get" is a reference to the
     * user stored in the map. This means that operations on this user i.e.
     * {@link User#setEmail(String)} would be reflected in our storage, bypassing
     * the {@link InMemoryUserStorage#updateUser(User)} method.
     */
    @Override
    public User loadUser(String email) {
	User userFromStorage = users.get(email);

	if (userFromStorage == null) {
	    return userFromStorage;
	}
	// I implement the copying the user here since I don't want to "polute" the user
	// implementation with it.
	User userCopy = new User();
	userCopy.setEmail(userFromStorage.getEmail());
	userCopy.setPasswordHash(userFromStorage.getPasswordHash());
	userCopy.setRole(userFromStorage.getRole());

	return userCopy;
    }

    @Override
    public void updateUser(User user) {
	users.replace(user.getEmail(), user);
    }

}
