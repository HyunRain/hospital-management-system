<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';
import { useToggleStore } from '@/stores/toggleStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const toggleStore = useToggleStore();
const router = useRouter();

async function handleLogout() {
  if(!authStore.user) {
    toggleStore.toggleDemo();
    router.push('/login');
    toggleStore.toggleUserDropdown();
  }
  await authStore.logout();
  router.push('/login');
  toggleStore.toggleUserDropdown();
}
</script>

<template>
  <div class="absolute right-0 top-10 z-1000 bg-white dark:bg-[#18191c] shadow-lg font-medium rounded-xl py-6  min-w-[200px] w-full max-w-[300px]">
  <ul class="flex flex-col gap-2 text-[14px]">
    <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-neutral-800 p-2.5 px-6 rounded-md transition">
      <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/profile.svg`" alt="Profile Icon" />
      <span>Profile</span>
    </li>
    <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-neutral-800 p-2.5 px-6 rounded-md transition">
      <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/settings.svg`" alt="Settings Icon" />
      <span>Settings</span>
    </li>
    <li @click="handleLogout()" class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-neutral-800 p-2.5 px-6 rounded-md transition">
      <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/logout.svg`" alt="Logout Icon" />
      <span>Logout</span>
    </li>
  </ul>
</div>
</template>

<style scoped>

</style>
