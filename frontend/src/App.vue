<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavBar from './components/layout/NavBar.vue';
import MainFooter from './components/layout/MainFooter.vue';
</script>

<template>
  <div class="flex flex-col min-h-screen">
    <header>
      <nav>
        <NavBar/>
      </nav>
    </header>

    <main class="flex min-h-[calc(100vh-106px)]">
      <RouterView/>
    </main>

    <footer>
      <MainFooter/>
    </footer>
  </div>
</template>

<style scoped>


</style>
