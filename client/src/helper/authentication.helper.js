import router from "../router";
import store from "../store";
import {
  login as loginAPI,
  refresh as refreshAPI,
  register as registerAPI
} from "../api/authentication.service.api";

/**
 * Checks if a user is currently logged in. Will attempt to update an expired access token and will
 * update the state with the currrent state of authentication.
 *
 * This method is called whenever a user tries to access a protected route. (But is safe to call
 * whenever the current state of authentication is not clear.)
 */
export var isAuthenticated = () => {
  return new Promise((resolve, reject) => {
    // Get everythin from local storage we need to determine if a user is authenticated
    let accessToken = loadAccessToken();
    let expiresAt = loadExpiresAt();
    let refreshToken = loadRefreshToken();

    // If one or more of these is missing a user is not authenticated
    if (!accessToken || !expiresAt || !refreshToken) {
      resolve(false);
    } else if (isExpired(expiresAt)) {
      // If the access token expired we try to refresh it. If this fails we assume the user to not be authenticated.
      refreshAccessToken()
        .then(result => {
          resolve(result);
        })
        .catch(err => {
          reject(err);
        });
    } else {
      resolve(true);
    }
  });
};

/**
 * Checks if the user is authenticated and will update the vuex state accordingly.
 */
export function updateAuthenticationState() {
  isAuthenticated()
    .then(result => {
      store.commit("setAuthenticated", result);
    })
    .catch(error => Promise.reject(error));
}

/**
 * Responsible to issue the login request to the server and handle the result.
 *
 * @param {String} email
 * @param {String} password
 */
export function login(email, password) {
  loginAPI(email, password)
    .then(response => {
      let isAuthenticated = handleAuthResponse(response);
      store.commit("setAuthenticated", isAuthenticated);
      if (isAuthenticated) {
        // TODO: Since this method is asynchrouse the user should see a loading indicator while waiting to be logged in.
        router.push("/");
      }
    })
    .catch(() => {
      store.commit("setAuthenticated", false);
      // TODO: Inform the user what went wrong
    });
}

export function register(email, password) {
  registerAPI(email, password)
    .then(response => {
      let isAuthenticated = handleAuthResponse(response);
      store.commit("setAuthenticated", isAuthenticated);
      if (isAuthenticated) {
        // TODO: Since this method is asynchrouse the user should see a loading indicator while waiting to be logged in.
        router.push("/");
      }
    })
    .catch(() => {
      store.commit("setAuthenticated", false);
      // TODO: Inform the user what went wrong
    });
}

/**
 * Logs the user out by deleting the token from local storage.
 */
export function logout() {
  removeAccessToken();
  removeExpiresAt();
  removeRefreshToken();

  store.commit("setAuthenticated", false);
  router.push("/login");
}

/**
 * Retruns the access token if available. Otherwise an empty string
 */
export function getAccessToken() {
  return loadAccessToken() !== null ? loadAccessToken() : "";
}

/**
 * Tries to obtain a new access token from the server using the refresh token. If no refresh token
 * present the user has to login or register first. The method is self contained and can be called
 * without any prerequisite.
 */
export function refreshAccessToken() {
  return new Promise((resolve, reject) => {
    let refreshToken = loadRefreshToken();

    // Without a refresh token there is nothing more to do
    if (!refreshToken) {
      reject(Error("No refresh token present"));
    }
    // Try to refresh the token
    refreshAPI(refreshToken)
      .then(response => {
        let isAuthenticated = handleAuthResponse(response);
        resolve(isAuthenticated);
      })
      .catch(err => {
        // If refreshing the token fails the user is logged out
        logout();
        reject(err);
      });
  });
}

/**
 * Will ensure and process that all data for a valid authentication is present
 * @param {Object} response
 */
function handleAuthResponse(response) {
  let { accessToken, expiresAt, refreshToken } = response.data;
  // If the status is not 200 and any of the information is missing the login was not successfull
  if (response.status !== 200 || !accessToken || !refreshToken || !expiresAt) {
    return false;
  } else {
    saveAccessToken(accessToken);
    saveExpiresAt(expiresAt);
    saveRefreshToken(refreshToken);
    return true;
  }
}

/**
 * Validates if the given date is before the current time.
 * @param {Date} expiresAt
 */
function isExpired(expiresAt) {
  let now = new Date();
  let expiresAtDate = new Date(expiresAt);
  return now > expiresAtDate;
}

/**
 * Load the access token from local storage.
 */
function loadAccessToken() {
  return localStorage.getItem("accessToken");
}

/**
 * Saves the access token to local storage.
 * @param {String} accessToken
 */
function saveAccessToken(accessToken) {
  localStorage.setItem("accessToken", accessToken);
}

/**
 * Remove the access token from local storage.
 */
function removeAccessToken() {
  localStorage.removeItem("accessToken");
}

/**
 * Load the "expires at" date from local storage. It represents the date when the access token
 * expires.
 */
function loadExpiresAt() {
  return localStorage.getItem("expiresAt");
}

/**
 * Saves the "expires at" date to local storage.
 * @param {String} expiresAt
 */
function saveExpiresAt(expiresAt) {
  localStorage.setItem("expiresAt", expiresAt);
}

/**
 * Remove "expires at" from local storage.
 */
function removeExpiresAt() {
  localStorage.removeItem("expiresAt");
}

/**
 * Load the refresh token from local storage.
 */
function loadRefreshToken() {
  return localStorage.getItem("refreshToken");
}

/**
 * Saves the refresh token to local storage.
 * @param {String} refreshToken
 */
function saveRefreshToken(refreshToken) {
  return localStorage.setItem("refreshToken", refreshToken);
}

/**
 * Remove refresh token from local storage.
 */
function removeRefreshToken() {
  localStorage.removeItem("refreshToken");
}
