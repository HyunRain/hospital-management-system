<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router';
import { useToggleStore } from '@/stores/toggleStore';
import { computed, watch } from 'vue';

const toggleStore = useToggleStore();
const route = useRoute();

const currentRoute = computed(() => route.path);
watch(route, (newRoute) => {
  if (!newRoute.path.startsWith('/dashboard/billings')) {
    toggleStore.showBillingDropdown = false;
  }
});
</script>

<template>
  <div class="flex flex-grow w-full pr-5 pl-5">
    <!-- Sidebar -->
    <div class="rounded-lg max-w-[220px] w-full text-start md:flex flex-col hidden md:fixed self-start top-[86px] mr-5">
      <ul class="flex flex-col gap-4 items-start w-full">
        <RouterLink to="/dashboard" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition w-full"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/dashboard.svg`" alt="Dashboard Icon">
            <span>Dashboard</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/doctors" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/doctors' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/doctor.svg`" alt="Dashboard Icon">
            <span>Doctors</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/patients" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/patients' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/patients.svg`" alt="Patients Icon">
            <span>Patients</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/appointments" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/appointments' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/appointment.svg`" alt="Appointment Icon">
            <span>Appointments</span>
          </li>
        </RouterLink>
        <!--<RouterLink to="/dashboard/bed-manager" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/bed-manager' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/patient.svg`" alt="Bed Manager Icon">
            <span class="pr-10">Bed Manager</span>
          </li>
        </RouterLink>-->
        <RouterLink to="/dashboard/departments" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/departments' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/department.svg`" alt="Department Icon">
            <span>Departments</span>
          </li>
        </RouterLink>
        <RouterLink to="/dashboard/staff" class="w-full">
          <li class="cursor-pointer flex items-center gap-3 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/staff' }">
            <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/employees.svg`" alt="Staff Icon">
            <span>Staff</span>
          </li>
        </RouterLink>
        <div class="w-full">
          <li
            class="cursor-pointer flex items-center justify-between select-none hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
            :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/billings' }" @click="toggleStore.toggleBillingDropdown">
            <div class="flex items-center gap-3">
              <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/billing.svg`" alt="Dashboard Icon">
              <span>Billings</span>
            </div>
            <img v-if="!toggleStore.showBillingDropdown" class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/rightarrow.svg`"
              alt="Dashboard Icon">
            <img v-else class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Dashboard Icon">
          </li>
          <section class="pl-1 w-full flex flex-col">
            <ul v-if="toggleStore.showBillingDropdown" class="pl-11 flex flex-col gap-2">
              <RouterLink to="/dashboard/billings/overview" class="w-full">
                <li class="cursor-pointer flex items-center mt-2 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
                  :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/billings/overview' }">Billing Overview</li>
              </RouterLink>
              <RouterLink to="/dashboard/billings/accounts" class="w-full">
                <li class="cursor-pointer flex items-center hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
                  :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/billings/accounts' }">Billing Accounts</li>
              </RouterLink>
              <RouterLink to="/dashboard/billings/prices" class="w-full">
                <li class="cursor-pointer flex items-center hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] p-2.5 rounded-lg transition"
                  :class="{ 'text-red-600 dark:text-[#e7523b]': currentRoute === '/dashboard/billings/prices' }">Billing Prices</li>
              </RouterLink>
            </ul>
          </section>
        </div>
      </ul>
    </div>

    <div class=" md:pl-[180px] xl:pl-[240px] flex w-full">
      <RouterView />
    </div>
  </div>
</template>

<style scoped></style>
