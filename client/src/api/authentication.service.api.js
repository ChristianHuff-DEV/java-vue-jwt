import axios from "axios";

import { AUTHENTICATION_SERVICE_URL } from "./api";

/**
 * Gets the users profil data from the user service. There is no need to provide the users id or
 * email. This will be extracted from the access tokens claims.
 */
export function getUser() {
  return axios(`${AUTHENTICATION_SERVICE_URL}/user`, {
    method: "get"
  });
}

/**
 * Tries to login the user with the given credentials.
 *
 * @param {String} email
 * @param {String} password
 */
export function register(email, password) {
  return axios({
    method: "post",
    url: `${AUTHENTICATION_SERVICE_URL}/register`,
    data: { email, password }
  });
}

/**
 * Tries to login the user with the given credentials.
 *
 * @param {String} email
 * @param {String} password
 */
export function login(email, password) {
  return axios({
    method: "post",
    url: `${AUTHENTICATION_SERVICE_URL}/login`,
    data: { email, password }
  });
}

/**
 * Asks the server to issue a new access token for the given refresh token.
 * @param {String} refreshToken
 */
export function refresh(refreshToken) {
  return axios({
    method: "post",
    url: `${AUTHENTICATION_SERVICE_URL}/refresh`,
    data: { refreshToken: refreshToken }
  });
}
