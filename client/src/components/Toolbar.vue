<template>
  <v-toolbar
    dark
    color="primary"
  >
    <v-toolbar-items>
      <v-btn
        flat
        to="/"
      >
        Home
      </v-btn>
    </v-toolbar-items>
    <v-spacer />
    <v-toolbar-items>
      <!-- Show register button only if user is not logged in -->
      <v-btn
        v-if="!isAuthenticated"
        flat
        to="/register"
      >
        Register
      </v-btn>
      <!-- Show login button only if user is not logged in -->
      <v-btn
        v-if="!isAuthenticated"
        flat
        to="/login"
      >
        Login
      </v-btn>
      <!-- Show profile button only if user is logged in -->
      <v-btn
        v-if="isAuthenticated"
        flat
        to="/profile"
      >
        Profile
      </v-btn>
      <!-- Show logout button only if user is logged in -->
      <v-btn
        v-if="isAuthenticated"
        flat
        @click="logout"
      >
        Logout
      </v-btn>
    </v-toolbar-items>
  </v-toolbar>
</template>

<script>
import { mapGetters } from "vuex"
import { logout } from "../helper/authentication.helper"

export default {
  computed: {
    ...mapGetters(["isAuthenticated"])
  },
  methods: {
    logout: logout
  }
}
</script>

<style>
</style>
