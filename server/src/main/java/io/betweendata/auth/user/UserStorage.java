package io.betweendata.auth.user;

/**
 * Interface which to be implemented by storage solutions that will save user
 * data.
 * 
 * @author christian
 *
 */
public interface UserStorage {

    public void saveUser(User user);

    public User loadUser(String email);

    public void updateUser(User user);

}
