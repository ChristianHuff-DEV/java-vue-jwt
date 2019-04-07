package io.betweendata.auth.user;

/**
 * Contains methods to validate data related to the user authentication process.
 * 
 * @author christian
 *
 */
public class UserAuthenticationValidator {

	/**
	 * Validates that the given email is a valid format.<br>
	 * 
	 * A valid email fulfills the following requirements:
	 * <ul>
	 * <li>Contains an "@" (at) symbol</li>
	 * <li>Contains a "." (dot)</li>
	 * <li>Has at least one character before the "@"</li>
	 * <li>Has at least one character between the "@" and "."</li>
	 * <li>Has at least to two characters after the "."</li>
	 * </ul>
	 * 
	 * @param email - the email address to validate
	 * @return
	 */
	public static boolean isEmailValid(String email) {
		return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
	}

	/**
	 * Validates that the given password is valid in the sense that it fulfills the
	 * following minimum requirements:
	 * <ul>
	 * <li>Consists out of at least 4 characters</li>
	 * </ul>
	 * 
	 * @param password - the password to validate
	 * @return
	 */
	public static boolean isPasswordValid(String password) {
		return password.length() >= 4;
	}
}
