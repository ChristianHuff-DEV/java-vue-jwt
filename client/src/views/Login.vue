<template>
  <authentication-form :is-login="true" />
</template>

<script>
import AuthenticationForm from '../components/Authentication.vue'
export default {
  components: {
    AuthenticationForm
  }
}
</script>

<style>
</style>
