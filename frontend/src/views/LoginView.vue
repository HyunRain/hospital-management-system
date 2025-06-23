<script setup lang="ts">
import { ref, onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { isAxiosError } from 'axios';
import { useToggleStore } from '@/stores/toggleStore';
import { validate } from '@/util/functions/validation/validate';
import { required, validEmail } from '@/util/functions/validation/rules';

const authStore = useAuthStore();
const toggleStore = useToggleStore();
const router = useRouter();

onBeforeMount(async () => {
  const authStore = useAuthStore();
  await authStore.reauthenticate();

  if (authStore.role && authStore.email) {
    await authStore.fetchStaffData(authStore.role, authStore.email);
  }
  if (authStore.user) {
    router.push('/dashboard'); // or your desired route
  }
});

const loginInput = ref<Record<string, string>>({
  email: '',
  password: '',
});

async function handleLogin() {
  const result = validate(loginInput.value, rules);
  if (!result.success) {
    triggerFrontendError(result.error);
    return;
  }

  try {
    await authStore.login(loginInput.value);
    if (authStore.role && authStore.email) {
      await authStore.fetchStaffData(authStore.role, authStore.email);
      authStore.isInitialised = true;
    }
    router.push({ path: '/dashboard' });
  } catch (error: unknown) {
    if (isAxiosError(error) && error.message.includes('Network Error')) {
      triggerBackendError("Login Service is currently unavailable.");
    }

    if (isAxiosError(error) && error.response?.status === 503) {
      triggerBackendError("Login Service is currently unavailable.");
    }

    if (isAxiosError(error) && error.response?.data?.message === "Invalid email or password.") {
      triggerBackendError(error.response?.data?.message);
    }
  }
}

// WIP
function handleDemoLogin() {
  toggleStore.toggleDemo();
  router.push('/dashboard');
}

// Rules for frontend validation used in handleLogin()
// rules.ts to check what rules you can apply.
// make sure key names are identical to data property keys.
const rules: Record<string, Array<(value: string) => boolean | string>> = {
  email: [required, validEmail],
  password: [required],
};

// ------------------------ Backend Error ------------------------

const showLoginFailed = ref(false);
const errorMessage = ref('');
const errorAlertKey = ref(Date.now());
let timer: ReturnType<typeof setTimeout>;

function triggerBackendError(message: string) {
  window.clearTimeout(timer);
  showLoginFailed.value = true;
  errorAlertKey.value = Date.now();
  errorMessage.value = message;
  timer = setTimeout(() => {
    showLoginFailed.value = false;
    errorMessage.value = '';
  }, 5000);
}

// ------------------------ Frontend Error ------------------------

const showBadLoginData = ref(false);
const badLoginDataMessage = ref('');
const badLoginDataAlertKey = ref(Date.now());
let badLoginDataTimer: ReturnType<typeof setTimeout>;

function triggerFrontendError(message: string) {
  window.clearTimeout(badLoginDataTimer);
  showBadLoginData.value = true;
  badLoginDataAlertKey.value = Date.now();
  badLoginDataMessage.value = message;
  badLoginDataTimer = setTimeout(() => {
    showBadLoginData.value = false;
    badLoginDataMessage.value = '';
  }, 5000);
}
</script>

<template>
  <div class="flex bg-white dark:bg-[#030712] min-h-[calc(100vh-127px)] w-full justify-center items-center">
    <div class="flex max-w-[1000px] justify-center w-full md:shadow-lg rounded-xl h-[65%]">
      <div class="w-1/2 bg-[#FFE0CA] p-5 items-center justify-center rounded-l-xl hidden md:flex">
        <img src="/assets/images/hospital image.png" alt="Hospital Management System Image" />
      </div>
      <div
        class="px-5 md:p-0 items-center justify-center border md:border-y md:border-r border-gray-100 dark:border-zinc-800 bg-white dark:bg-[#030713] flex flex-col md:w-1/2 rounded-xl md:rounded-none md:rounded-r-xl">
        <h2 class="mb-10 font-bold text-[28px]">Login to Dashboard</h2>
        <form class="flex flex-col items-center justify-center gap-6 w-full md:w-2/4" @submit.prevent="handleLogin()">
          <input
            class="bg-white dark:bg-[#030714] placeholder-[#828282] w-full px-[10px] py-[7px] rounded-xl border border-gray-300 dark:border-zinc-800 focus:border-red-300 dark:focus:border-zinc-600 focus:outline-none"
            type="text" name="email" placeholder="Email" v-model="loginInput.email" />
          <div class="relative w-full">
            <input
              class="bg-white dark:bg-[#030714] placeholder-[#828282] w-full px-[10px] py-[7px] rounded-xl border border-gray-300 dark:border-zinc-800 focus:border-red-300 dark:focus:border-zinc-600 focus:outline-none"
              :type="toggleStore.showPassword ? 'text' : 'password'" name="password" placeholder="Password" v-model="loginInput.password" />
            <img v-if="toggleStore.showPassword" class="size-5 cursor-pointer absolute top-1/2 right-3 -translate-y-1/2"
              :src="`/assets/icons/${toggleStore.darkModeState}/eyeHide.svg`" alt="Show Eye Password Icon" @click="toggleStore.togglePassword">
            <img v-else class="size-5 cursor-pointer absolute top-1/2 right-3 -translate-y-1/2"
              :src="`/assets/icons/${toggleStore.darkModeState}/eyeShow.svg`" alt="Hide Eye Password Icon" @click="toggleStore.togglePassword">
          </div>
          <button
            class="w-full px-[10px] py-[7px] rounded-xl font-medium bg-[#ffb192] dark:bg-[#ff9870] text-[#212121] shadow-lg hover:bg-[#eecfba] border border-[#ffd8be3b] cursor-pointer"
            type="submit">
            Login
          </button>
        </form>
        <!-- Backend Error -->
        <transition enter-active-class="transition-transform transition-opacity duration-500 ease-out" enter-from-class="-translate-x-10 opacity-0"
          enter-to-class="translate-x-0 opacity-100">
          <p v-if="showLoginFailed" :key="errorAlertKey" class="mt-5 text-red-600"> {{ errorMessage }} </p>
        </transition>
        <!-- Frontend Error -->
        <transition enter-active-class="transition-transform transition-opacity duration-200 ease-out" enter-from-class="-translate-x-10 opacity-0"
          enter-to-class="translate-x-0 opacity-100">
          <p v-if="showBadLoginData" :key="badLoginDataAlertKey" class="mt-5 text-red-600"> {{ badLoginDataMessage }}
          </p>
        </transition>
        <p @click="handleDemoLogin()" class="mt-5 cursor-pointer underline hover:text-[#a7a7a7]">Explore as a Demo Admin
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
