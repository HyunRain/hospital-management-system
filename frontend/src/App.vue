<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './components/NavBar.vue';
import MainFooter from './components/MainFooter.vue';
</script>

<template>
  <header>
    <nav>
      <NavBar/>
    </nav>
  </header>

  <main>
    <RouterView/>
  </main>

  <footer>
    <MainFooter/>
  </footer>
</template>

<style scoped>


</style>
