<script setup lang="ts">
import { useStaffStore } from '@/stores/staffStore';

const staffStore = useStaffStore();


</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr v-if="staffStore.staff.length === 0">
        <td colspan="7" class="text-center py-4">
          No results found.
        </td>
      </tr>

      <tr v-else class=" h-[30px] text-[15px]">
        <th class="md:px-0 px-13 w-[200px]">Name</th>
        <th class="w-[150px]">Staff ID</th>
        <th class="md:px-0 px-5 w-[150px]">Gender</th>
        <th class="md:px-0 px-3 w-[130px]">Birthdate</th>
        <th class="w-[260px]">Email</th>
        <th class="w-[150px]">City</th>
        <th class="w-[150px]">Role</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(staff, index) in staffStore.staff"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf]">
        <td class="border-0 rounded-l-xl"> {{ staff.firstName + ' ' + staff.lastName }} </td>
        <td>{{ staff.staffId }}</td>
        <td>{{ staff.gender }}</td>
        <td> {{ staff.dateOfBirth }}</td>
        <td> {{ staff.email }}</td>
        <td class="min-w-[80px]"> {{ staff.city }}</td>
        <td class="border-0 rounded-r-xl"> {{ staff.role }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped></style>
