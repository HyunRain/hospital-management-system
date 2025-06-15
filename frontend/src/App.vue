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

  <main class="flex flex-col h-[calc(100vh-123px)]">
    <RouterView/>
  </main>

  <footer>
    <MainFooter/>
  </footer>
</template>

<style scoped>


</style>
