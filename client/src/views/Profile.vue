<template>
  <p>{{ getUser }}</p>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["getUser"])
  },
  created () {
    this.$store.dispatch("getUser")
  }

}
</script>

<style>
</style>
