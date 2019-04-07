<template>
  <authentication-form :is-login="false" />
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
