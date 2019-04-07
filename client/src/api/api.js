import axios from "axios";
import {
  getAccessToken,
  refreshAccessToken
} from "../helper/authentication.helper";

export const AUTHENTICATION_SERVICE_URL = "http://localhost:7000";

/**
 * Initializes axios
 *
 * This includes to set it up to include an authorization header when sending requests to the backend.
 */
export function initAxios() {
  // Add Authorization header to all requests going to the specified url
  axios.interceptors.request.use(
    config => {
      // Add the access token to each request whos URL starts with the one defined here
      if (config.url.startsWith(AUTHENTICATION_SERVICE_URL)) {
        let token = getAccessToken();
        // If a token is present add it to the header
        if (token) {
          // Add the Authorization header (make sure there is a space (" ") between "Bearer" and the token)
          config.headers.common.Authorization = "Bearer " + token;
        }
        // If no token was found the header does not include the access token. We don't want to deal with this in the client, but have the server tell us that it is missing.
        return config;
      }
    },
    error => Promise.reject(error)
  );

  // Deal with all responses containing a 401 error to try to refresh the access token if possible
  axios.interceptors.response.use(
    // No special handling of responses needed. We return it as it comes in.
    response => {
      return response;
    },
    // This object is not null if an error occured
    error => {
      // Check if it is a 401 Unauthorized error
      if (error.response.status === 401) {
        // Try to refresh the access token
        return refreshAccessToken()
          .then(result => {
            // Was refreshing the access token successfull?
            if (result === true) {
              // Repeat the request
              return axios({
                method: error.config.method,
                url: error.config.url,
                data: error.config.data
              });
            } else {
              // If the access token could not be refreshed we reject the promise and the code responsible for the request has to handle it.
              return Promise.reject(Error("Unauthorized"));
            }
          })
          .catch(error => Promise.reject(error));
      }
      // No special treatement of any other error
      return Promise.reject(error);
    }
  );
}
