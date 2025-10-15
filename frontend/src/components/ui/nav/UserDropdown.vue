<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';
import { useToggleStore } from '@/stores/toggleStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const toggleStore = useToggleStore();
const router = useRouter();

async function handleLogout() {
  await authStore.logout();
  router.push('/login');
  toggleStore.toggleUserDropdown();
}
</script>

<template>
  <div
    class="absolute right-0 top-13 z-50 bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900 shadow-lg font-medium rounded-lg py-3  min-w-[175px] w-full max-w-[300px]">
    <ul class="flex flex-col gap-2 text-[14px]">
      <!--<li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2.5 px-8 rounded-md transition">
        <img class="w-6 h-6" :src="`/assets/icons/${toggleStore.darkModeState}/profile.svg`" alt="Profile Icon" />
        <span>Profile</span>
      </li>
      <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2.5 px-8 rounded-md transition">
        <img class="w-6 h-6" :src="`/assets/icons/${toggleStore.darkModeState}/settings.svg`" alt="Settings Icon" />
        <span>Settings</span>
      </li>-->
      <li @click="handleLogout()"
        class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2.5 px-8 rounded-md transition">
        <img class="w-6 h-6" :src="`/assets/icons/${toggleStore.darkModeState}/logout.svg`" alt="Logout Icon" />
        <span>Logout</span>
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
