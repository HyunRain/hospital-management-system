<script setup lang="ts">
import { useDoctorStore } from '@/stores/doctorStore';

const doctorStore = useDoctorStore();


</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr class=" h-[30px]">
        <th class="md:px-0 px-13">Name</th>
        <th>Staff ID</th>
        <th class="md:px-0 px-5">Gender</th>
        <th class="md:px-0 px-3">Birthdate</th>
        <th>Email</th>
        <th>City</th>
        <th>Department</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(doctor, index) in doctorStore.doctors"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf]">
        <td class="border-0 rounded-l-xl"> {{ doctor.firstName + ' ' + doctor.lastName }} </td>
        <td>{{ doctor.staffId }}</td>
        <td>{{ doctor.gender }}</td>
        <td> {{ doctor.dateOfBirth }}</td>
        <td> {{ doctor.email }}</td>
        <td class="min-w-[80px]"> {{ doctor.city }}</td>
        <td class="border-0 rounded-r-xl"> {{ doctor.departmentName }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped></style>
