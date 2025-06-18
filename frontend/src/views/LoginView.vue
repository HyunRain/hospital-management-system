<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { isAxiosError } from 'axios'
import { useToggleStore } from '@/stores/toggleStore'

const authStore = useAuthStore()
const toggleStore = useToggleStore();
const router = useRouter()

const loginInput = ref({
  email: '',
  password: '',
})

async function handleLogin() {
  try {
    await authStore.login(loginInput.value)
    if (authStore.role && authStore.email) {
      console.log('test 123 login')
      await authStore.fetchStaffData(authStore.role, authStore.email)
      authStore.isInitialised = true
    }
    router.push({ path: '/dashboard' })
  } catch (error: unknown) {
    if (
      typeof error === 'object' &&
      error !== null &&
      'message' in error &&
      typeof (error as any).message === 'string' &&
      (error as any).message.includes('net::ERR_CONNECTION_REFUSED')
    ) {
      console.error('Login service is currently not available')
    }
    if (isAxiosError(error) && error.response?.data?.message) {
      console.error('Login failed: ', error.response.data.message)
    }
  }
}

function handleDemoLogin() {
  toggleStore.toggleDemo();
  router.push('/dashboard');
}
</script>

<template>
  <div class="flex bg-zinc-50 dark:bg-zinc-900 w-full justify-center items-center">
    <div class="flex max-w-[1000px] justify-center w-full md:shadow-lg rounded-xl h-[65%]">
      <div class="w-1/2 bg-[#FFE0CA] p-5 items-center justify-center rounded-l-xl hidden md:flex">
        <img src="/assets/images/hospital image.png" alt="Hospital Management System Image" />
      </div>
      <div
        class="px-5 md:p-0 items-center justify-center bg-white dark:bg-[#1c1c20] flex flex-col md:w-1/2 rounded-r-xl"
      >
        <h2 class="mb-10 font-bold text-[28px]">Login to Dashboard</h2>
        {{ toggleStore.isDemo }}
        <form
          class="flex flex-col items-center justify-center gap-6 w-full md:w-2/4"
          @submit.prevent="handleLogin()"
        >
          <input
            class="bg-white dark:bg-[#eeeeee] text-[#212121] placeholder-[#4f4f4f] w-full px-[10px] py-[7px] rounded-xl border border-gray-300 dark:border-0 dark:focus:ring-0 focus:ring-1 focus:outline-none focus:ring-red-300 transition"
            type="email"
            name="email"
            placeholder="Email"
            v-model="loginInput.email"
            required
          />
          <input
            class="bg-white dark:bg-[#eeeeee] text-[#212121] placeholder-[#4f4f4f] w-full px-[10px] py-[7px] rounded-xl border border-gray-300 dark:border-0 dark:focus:ring-0 focus:ring-1 focus:outline-none focus:ring-red-300 transition"
            type="password"
            name="password"
            placeholder="Password"
            v-model="loginInput.password"
            required
          />
          <button
            class="w-full px-[10px] py-[7px] rounded-xl font-medium bg-[#ffb192] dark:bg-[#ff9870] text-[#212121] shadow-lg hover:bg-[#eecfba] border border-[#ffd8be3b] cursor-pointer"
            type="submit"
          >
            Login
          </button>
        </form>
        <p @click="handleDemoLogin()" class="mt-5 cursor-pointer underline hover:text-[#a7a7a7]">Explore as a Demo Admin</p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
