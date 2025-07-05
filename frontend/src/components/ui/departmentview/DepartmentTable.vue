<script setup lang="ts">
import { useDepartmentStore } from '@/stores/departmentStore';

const departmentStore = useDepartmentStore();


</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr class=" h-[30px] text-[15px]">
        <th class="md:px-0 px-13">Department</th>
        <th>Head of Dept</th>
        <th class="md:px-0 px-5">Staff Count</th>
        <th class="md:px-0 px-3">Bed Capacity</th>
        <th>Current Bed Count</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(department, index) in departmentStore.departments" :key="index"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf]">
        <td class="border-0 rounded-l-xl"> {{ department.name }} </td>
        <td>{{ department.headOfDepartmentName }}</td>
        <td>{{ department.staffCount }}</td>
        <td> {{ department.bedCapacity }}</td>
        <td> {{ department.currentBedCount }}</td>
        <td class="border-0 rounded-r-xl">
          <div class="items-center flex gap-2 justify-center">
            <span class="inline-block size-3 rounded-full" :class="{'bg-green-500': department.isActive, 'bg-red-500': !department.isActive}"></span>
            <span>{{ department.isActive ? 'Open' : 'Closed' }}</span>
          </div>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped></style>
