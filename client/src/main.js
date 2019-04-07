import Vue from "vue";
import "./plugins/vuetify";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import { initAxios } from "./api/api";
import { updateAuthenticationState } from "./helper/authentication.helper";

Vue.config.productionTip = false;

initAxios();

new Vue({
  router,
  store,
  // Will be called whenever the vue instance is (re-)created. i.e. when the user refreshes the page
  created() {
    updateAuthenticationState();
  },
  render: h => h(App)
}).$mount("#app");
