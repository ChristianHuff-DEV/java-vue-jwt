<template>
  <p>Is user logged in: {{ isAuthenticated }}</p>
</template>

<script>
import { mapGetters } from "vuex"

export default {
  computed: {
    ...mapGetters(["isAuthenticated"])
  }
}
</script>
