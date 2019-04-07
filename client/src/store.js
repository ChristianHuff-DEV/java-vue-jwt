import Vue from "vue";
import Vuex from "vuex";
import { getUser } from "./api/authentication.service.api";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    /**
     * If a user is currrently logged in
     */
    isAuthenticated: false,
    /**
     * Informations about the current user
     */
    user: {}
  },
  mutations: {
    setAuthenticated: (state, isAuthenticated) => {
      state.isAuthenticated = isAuthenticated;
    },
    setUser: (state, user) => {
      state.user = user;
    }
  },
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    getUser: state => state.user
  },
  actions: {
    getUser: ({ commit }) => {
      getUser()
        .then(response => {
          commit("setUser", response.data);
        })
        .catch(() => commit("setUser", {}));
    }
  }
});
