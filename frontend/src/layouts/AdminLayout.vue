<script setup lang="ts">
import { RouterLink } from 'vue-router';
import { useToggleStore } from '@/stores/toggleStore';

const toggleStore = useToggleStore();
</script>

<template>
  <div class="flex flex-grow w-full px-5 py-5">

    <div class="rounded-lg max-w-[260px] w-full text-start md:flex flex-col hidden">
      <ul class="flex flex-col gap-4 items-start font-medium w-full">
        <RouterLink to="/dashboard">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition w-full">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/dashboard.svg`" alt="Dashboard Icon">
            <span class="pr-10">Dashboard</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/doctors">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/doctor.svg`" alt="Dashboard Icon">
            <span class="pr-10" >Doctors</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/patients">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/patients.svg`" alt="Dashboard Icon">
            <span class="pr-10">Patients</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/appointments">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/appointment.svg`" alt="Dashboard Icon">
            <span class="pr-10">Appointments</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/bed-manager">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/patient.svg`" alt="Dashboard Icon">
            <span class="pr-10">Bed Manager</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/departments">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/department.svg`" alt="Dashboard Icon">
            <span class="pr-10">Departments</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/employees">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/employees.svg`" alt="Dashboard Icon">
            <span class="pr-10">Employees</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/billings">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-red-100 dark:hover:bg-zinc-800 p-2.5 rounded-xl transition">
            <img class="w-8 h-7" :src="`/assets/icons/${toggleStore.darkModeState}/billing.svg`" alt="Dashboard Icon">
            <span class="pr-10">Billings</span>
          </li>
        </RouterLink>
      </ul>
    </div>

    <RouterView />
  </div>
</template>

<style scoped></style>
