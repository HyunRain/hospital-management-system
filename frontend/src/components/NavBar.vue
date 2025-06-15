<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();
</script>

<template>
  <div class="top-0 z-50 flex items-center justify-between px-10 py-3 bg-white position-sticky">
    <div class="flex items-center gap-50">
      <div class="flex items-center gap-2">
        <img src="@/assets/icons/hospital.svg" alt="Brand logo" class="w-[35px] h-[35px]">
        <h1 class="font-bold text-2xl">Med Admin</h1>
      </div>
      <input v-if="authStore.isLoggedIn" type="text" placeholder="Search ..." class="w-[250px] h-[35px] px-3 border border-gray-300 rounded-2xl focus:outline-none focus:border-gray-400">
    </div>
    <div v-if="authStore.isLoggedIn" class="flex items-center gap-3">
      <img src="@/assets/images/Johan.png" alt="Profile Picture" class="w-[35px] h-[35px] rounded-full object-fit-cover">
      <img src="@/assets/icons/arrow-down.svg" alt="Menu Icon" class="w-[13px] h-[13px] cursor-pointer">
    </div>
  </div>
</template>

<style scoped>

</style>
