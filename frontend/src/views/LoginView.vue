<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';

const authStore = useAuthStore();
const router = useRouter();

const loginInput = ref({
  email: '',
  password: '',
});

function handleLogin() {

};

</script>

<template>
  <div class="flex max-w-[1000px] w-full h-[65%] mx-auto my-auto shadow-lg rounded-xl">
    <div class="w-1/2 bg-[#FFE0CA] p-5 flex items-center justify-center rounded-bl-xl rounded-tl-xl">
      <img src="@/assets/images/hospital image.png" alt="Hospital Management System Image" />
    </div>

    <div class="bg-white items-center justify-center flex flex-col w-1/2 rounded-br-xl rounded-tr-xl">
      <h2 class="mb-10 font-bold text-2xl">Login to Dashboard</h2>
      <form class="flex flex-col items-center justify-center gap-6" @submit.prevent="handleLogin()">
        <input
          class="bg-white px-[10px] py-[5px] rounded-xl border border-gray-300 focus:ring-1 focus:outline-none focus:ring-red-300 transition"
          type="email" name="email" placeholder="Email" v-model="loginInput.email" required />
        <input
          class="bg-white px-[10px] py-[5px] rounded-xl border border-gray-300 focus:ring-1 focus:outline-none focus:ring-red-300 transition"
          type="password" name="password" placeholder="Password" v-model="loginInput.password" required />
        <button
          class="w-[221px] px-[10px] py-[5px] rounded-xl font-medium bg-[#FFE0CA] shadow-lg hover:bg-[#eecfba] border border-[#ffd8be3b] cursor-pointer"
          type="submit">
          Login
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
