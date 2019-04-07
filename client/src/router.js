import Vue from "vue";
import Router from "vue-router";
import { isAuthenticated } from "./helper/authentication.helper";
import Home from "./views/Home.vue";
import Register from "./views/Register.vue";
import Login from "./views/Login.vue";
import Profile from "./views/Profile.vue";

Vue.use(Router);

export const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "home",
      component: Home,
      meta: {
        isProtected: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        isProtected: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        isProtected: false
      }
    },
    {
      path: "/profile",
      name: "profile",
      component: Profile,
      meta: {
        isProtected: true
      }
    },
    {
      path: "/about",
      name: "about",
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () =>
        import(/* webpackChunkName: "about" */ "./views/About.vue"),
      meta: {
        isProtected: false
      }
    }
  ]
});

router.beforeEach((to, from, next) => {
  // If the target route is not protected we just send the user there
  if (!to.meta.isProtected) {
    next();
  }
  // If the route is protected we have to check if the user is authenticated
  else {
    isAuthenticated().then(result => {
      // Ensure we actually having a boolean and not just a truethy value
      if (result === true) {
        next();
      } else {
        next("/login");
      }
    });
  }
});

export default router;
