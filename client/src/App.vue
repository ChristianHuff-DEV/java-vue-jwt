<template>
  <v-app>
    <toolbar />
    <v-content>
      <v-content>
        <router-view />
      </v-content>
    </v-content>
  </v-app>
</template>

<script>
import Toolbar from "./components/Toolbar.vue"

export default {
  components: {
    Toolbar
  }
}
</script>
