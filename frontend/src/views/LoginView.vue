<script setup lang="ts">
import { ref, onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { isAxiosError } from 'axios';
import { useToggleStore } from '@/stores/toggleStore';
import { validate } from '@/util/functions/validation/validate';
import { required, validEmail } from '@/util/functions/validation/rules';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';

const authStore = useAuthStore();
const toggleStore = useToggleStore();
const router = useRouter();

const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();
const { showError: showLoginError, errorMessage: loginErrorMessage, errorAlertKey: loginErrorAlertKey, triggerBackendError: triggerFrontendError } = useErrorAlert();

onBeforeMount(async () => {
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
    if (
      isAxiosError(error) &&
      (error.message.includes('Network Error') || error.response?.status === 503)
    ) {
      triggerBackendError("Login Service is currently unavailable.");
    }

    if (isAxiosError(error) && error.response?.status === 429) {
      triggerBackendError("Too many login attempts. Please try again later.");
    }

    if (isAxiosError(error) && error.response?.data?.message === "Invalid email or password.") {
      triggerBackendError(error.response?.data?.message);
    }
  }
}

// Rules for frontend validation used in handleLogin()
// rules.ts to check what rules you can apply.
const rules: Record<string, Array<(value: string) => boolean | string>> = {
  email: [required, validEmail],
  password: [required],
};
</script>

<template>
  <main class="flex bg-white dark:bg-[#000] min-h-[calc(100vh-127px)] w-full justify-center items-center">
    <section class="flex max-w-[1000px] justify-center w-full md:shadow-lg rounded-lg h-[65%]">
      <figure class="w-1/2 bg-[#ffe2cb]  bg bg-gradient-to-b p-5 items-center justify-center rounded-l-lg hidden md:flex">
        <img src="/assets/images/hospital-image2.png" alt="Brand Logo">
      </figure>

      <div class="px-5 md:p-0 items-center justify-center border md:border-y
      md:border-r border-gray-100 dark:border-zinc-800 bg-white dark:bg-[#000]
      flex flex-col md:w-1/2 rounded-lg md:rounded-none md:rounded-r-lg">

        <header>
          <h2 class="mb-10 font-bold text-[28px]">Login to Dashboard</h2>
        </header>

        <form class="flex flex-col items-center justify-center gap-6 w-full md:w-2/4" @submit.prevent="handleLogin()">
          <label class="sr-only" for="email">Email</label>
          <input id="email" class="input" type="text" name="email" placeholder="Email" v-model="loginInput.email" />

          <div class="relative w-full">
            <label class="sr-only" for="password">Password</label>
            <input id="password" class="input" :type="toggleStore.showPassword ? 'text' : 'password'" name="password" placeholder="Password"
              v-model="loginInput.password" />
            <img v-if="toggleStore.showPassword" class="size-5 cursor-pointer absolute top-1/2 right-3 -translate-y-1/2"
              :src="`/assets/icons/${toggleStore.darkModeState}/eyeHide.svg`" alt="Show Eye Password Icon" @click="toggleStore.togglePassword">
            <img v-else class="size-5 cursor-pointer absolute top-1/2 right-3 -translate-y-1/2"
              :src="`/assets/icons/${toggleStore.darkModeState}/eyeShow.svg`" alt="Hide Eye Password Icon" @click="toggleStore.togglePassword">
          </div>

          <button class="w-full px-[10px] py-[7px] rounded-lg font-medium bg-[#fe936c] hover:bg-[#ff875b] text-[#212121] shadow-lg cursor-pointer"
            type="submit">
            Login
          </button>
        </form>

        <ErrorAlert for="backend error" class="mt-5" :alert-key="errorAlertKey" :show="showError" :message="errorMessage" role="alert"></ErrorAlert>
        <ErrorAlert for="frontend error" class="mt-5" :alert-key="loginErrorAlertKey" :show="showLoginError" :message="loginErrorMessage"
          role="alert"></ErrorAlert>
      </div>
    </section>
  </main>
</template>
