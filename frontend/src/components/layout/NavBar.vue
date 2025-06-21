<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore';
import { useToggleStore } from '@/stores/toggleStore';
import UserDropdown from '@/components/ui/nav/UserDropdown.vue';
import { roleToPascalCase } from '@/util/functions/roleToPascalCase';
import { Icon } from '@iconify/vue'
import { useColorMode } from '@vueuse/core'
import router from '@/router';

const authStore = useAuthStore();
const toggleStore = useToggleStore();

const mode = useColorMode({
  disableTransition: false,
});

</script>

<template>
  <div class="top-0 z-50 w-full flex items-center h-[66px] bg-[#fff] dark:bg-[#030712] justify-between px-2 md:px-7.5 py-3 fixed">
    <div class="flex items-center gap-30">
      <div class="flex items-center gap-2">
        <img src="/assets/icons/lightMode/hospital.svg" alt="Brand logo" class="w-[35px] h-[35px]">
        <h1 class="font-bold text-2xl">Med Admin</h1>
      </div>
      <input v-if="authStore.isLoggedIn" type="text" placeholder="Search ..." class="h-[35px] w-[220px] shadow-sm hidden md:block focus:w-[280px] px-3 border border-gray-300 dark:border-0 dark:bg-[#1f1f23] dark:placeholder-[#979797] rounded-xl focus:outline-none">
    </div>

    <div class="flex items-center gap-4 relative">
      <div class="mr-4">
        <Icon v-if="mode === 'light'" @click="mode = 'dark', toggleStore.darkModeState = 'darkMode'" icon="radix-icons:moon" class="cursor-pointer h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-100"></Icon>
        <Icon v-if="mode === 'dark'" @click="mode = 'light', toggleStore.darkModeState = 'lightMode'" icon="radix-icons:sun" class="cursor-pointer h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"></Icon>
        <span class="sr-only">Toggle theme</span>
      </div>
      <div v-if="authStore.isLoggedIn" class="flex gap-4 items-center">
        <div class="md:flex flex-col items-center hidden">
          <p class="dark:text-neutral-400 text-gray-500"> {{ authStore.role ? roleToPascalCase(authStore.role) : "" }} </p>
          <p class="font-medium">{{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</p>
        </div>
        <img @click="toggleStore.toggleUserDropdown()" :src="authStore.profileImage" alt="Profile Picture" class="w-[35px] h-[35px] rounded-full object-fit-cover cursor-pointer">
        <UserDropdown v-if="toggleStore.showUserDropdown" v-click-outside="toggleStore.toggleUserDropdown"/>
      </div>
      <div v-if="toggleStore.isDemo" class="flex items-center gap-4">
        <div class="flex flex-col">
          <p class="hidden md:block">Demo Session</p>
          <p class="md:hidden">Demo</p>
        </div>
        <img @click="toggleStore.toggleUserDropdown()" :src="authStore.profileImage" alt="Profile Picture" class="w-[35px] h-[35px] rounded-full object-fit-cover cursor-pointer">
        <UserDropdown v-if="toggleStore.showUserDropdown" v-click-outside="toggleStore.toggleUserDropdown"/>
      </div>

    </div>
  </div>
</template>

<style scoped>

</style>
