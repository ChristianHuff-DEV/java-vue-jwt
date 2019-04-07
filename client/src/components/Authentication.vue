<template>
  <div class="container">
    <!-- "login" is the method that will be called once the element of type "submit" has been clicked -->
    <v-form @submit.prevent="onSubmit">
      <v-text-field
        v-model="email"
        label="Email"
        required
      />
      <v-text-field
        v-model="password"
        type="password"
        label="Password"
        required
      />
      <v-btn
        color="primary"
        type="submit"
      >
        {{ isLogin ? "Login" : "Register" }}
      </v-btn>
    </v-form>
  </div>
</template>

<script>
import { login, register } from "../helper/authentication.helper"

export default {
  props: {
    isLogin: {
      type: Boolean,
      required: true
    }
  },
  data () {
    return {
      email: "",
      password: ""
    }
  },
  methods: {
    onSubmit () {
      if (this.isLogin) {
        this.login()
      } else {
        this.register()
      }
    },
    register () {
      register(this.email, this.password)
    },
    login () {
      login(this.email, this.password)
    }
  }
}
</script>

<style scoped>
.container {
  width: 400px;
  margin: 8rem auto 0;
  text-align: center;
}
</style>
