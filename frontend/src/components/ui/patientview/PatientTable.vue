<script setup lang="ts">
import { usePatientStore } from '@/stores/patientStore';

const patientStore = usePatientStore();


</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr v-if="patientStore.patients.length === 0">
        <td colspan="7" class="text-center py-4">
          No results found.
        </td>
      </tr>

      <tr v-else class=" h-[30px] text-[15px]">
        <th class="md:px-0 px-13">Name</th>
        <th>Patient ID</th>
        <th class="md:px-0 px-5">Gender</th>
        <th class="md:px-0 px-3">Birthdate</th>
        <th>Email</th>
        <th>City</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(patient, index) in patientStore.patients"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf]">
        <td class="border-0 rounded-l-lg"> {{ patient.firstName + ' ' + patient.lastName }} </td>
        <td>{{ patient.patientId }}</td>
        <td>{{ patient.gender }}</td>
        <td> {{ patient.dateOfBirth }}</td>
        <td> {{ patient.email }}</td>
        <td class="min-w-[80px]"> {{ patient.city }}</td>
        <td class="border-0 rounded-r-lg"> {{ patient.status }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
</style>
